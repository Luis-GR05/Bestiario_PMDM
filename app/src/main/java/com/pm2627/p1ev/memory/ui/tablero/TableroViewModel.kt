package com.pm2627.p1ev.memory.ui.tablero

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pm2627.p1ev.memory.model.Carta
import com.pm2627.p1ev.memory.model.EstadoCarta
import com.pm2627.p1ev.memory.model.Nivel
import com.pm2627.p1ev.memory.model.repartir
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

/**
 * EL VIEWMODEL DE UNA PARTIDA (titular de estado o "State Holder").
 * Aquí está la fuente única de verdad de la partida y TODAS las reglas del juego.
 *
 * Patrón UDF (apuntes 3C.1 §3):
 *  - Estado hacia abajo: el ViewModel expone el TableroUiState y la pantalla lo dibuja.
 *  - Eventos hacia arriba: la pantalla avisa con funciones on...() (por ejemplo onCartaPulsada(id)).
 *
 * El ViewModel sobrevive al giro del móvil (3C.1 §1.1), así que la partida no se pierde.
 * No importa nada de Compose: se prueba con tests de JVM (3C.4 §4).
 *
 * Por constructor recibo el azar y el retardo del fallo (por defecto, el del nivel).
 * En los tests paso Random(42) para que el reparto sea siempre el mismo.
 */
class TableroViewModel(
    private val nivel: Nivel,
    private val random: Random = Random.Default,
    private val retardoFalloMs: Long = nivel.retardoFalloMs,
) : ViewModel() {

    // 1. Estado mutable privado (solo modificable dentro del ViewModel)
    private val _uiState = MutableStateFlow(TableroUiState())

    // 2. Estado público inmutable (solo lectura para la interfaz)
    val uiState: StateFlow<TableroUiState> = _uiState.asStateFlow()

    // No son datos de pantalla: no van en el UiState (3C.1 §5.1)
    private var jobCronometro: Job? = null
    private var jobFallo: Job? = null

    init {
        onNuevaPartida()
    }

    /** Evento: nueva partida. Reparte y baraja otra vez (también lo usa el botón "Reiniciar"). */
    fun onNuevaPartida() {
        jobCronometro?.cancel()
        jobFallo?.cancel()
        _uiState.value = TableroUiState(cartas = repartir(nivel, random))
    }

    /**
     * Evento: el usuario ha pulsado una carta. Las reglas, EN ESTE ORDEN:
     *   1. Tablero bloqueado o partida ganada: no hace nada.
     *   2. La carta no existe o no está boca abajo (ya girada o emparejada): no hace nada.
     *   3. Es la primera del turno: se pone boca arriba (y arranca el tiempo si es la primera de la partida).
     *   4. Es la segunda: se pone boca arriba y cuenta UN movimiento.
     *        a. Pareja: las dos quedan emparejadas. Si era la última, partida ganada.
     *        b. No pareja: se bloquea el tablero, se espera y se vuelven a poner boca abajo.
     */
    fun onCartaPulsada(id: Int) {
        val estado = _uiState.value
        // regla: ignora la pulsación durante la espera (3C.1 §5.2)
        if (estado.bloqueado || estado.ganada) return                       // 1

        val carta = estado.cartas.find { it.id == id } ?: return            // 2
        if (!carta.bocaAbajo) return

        val primera = estado.cartasBocaArriba.firstOrNull()
        if (primera == null) {                                              // 3
            // Actualizamos el estado con una copia inmutable
            _uiState.update { it.copy(cartas = it.cartas.conEstado(EstadoCarta.BOCA_ARRIBA, id), empezada = true) }
            if (!estado.empezada) arrancarCronometro()
            return
        }

        val movimiento = estado.movimientos + 1                             // 4
        if (primera.esParejaDe(carta)) {                                    // 4a
            val cartas = estado.cartas.conEstado(EstadoCarta.EMPAREJADA, primera.id, id)
            val ganada = cartas.all { it.emparejada }
            if (ganada) jobCronometro?.cancel() // el tiempo se para al ganar
            _uiState.update {
                it.copy(
                    cartas = cartas,
                    movimientos = movimiento,
                    ganada = ganada,
                    ultimaJugada = Jugada(if (ganada) TipoJugada.VICTORIA else TipoJugada.PAREJA, movimiento),
                    puntuacion = it.puntuacion + 10 + (5 * it.rachaTotal),
                    racha = true,
                    rachaTotal = it.rachaTotal + 1,
                    mejorRacha = maxOf(it.mejorRacha, it.rachaTotal + 1),
                )
            }
        } else {                                                            // 4b
            // el bloqueo, antes de esperar: va en el mismo cambio de estado que gira la carta (3C.1 §5.2)
            _uiState.update {
                it.copy(
                    cartas = it.cartas.conEstado(EstadoCarta.BOCA_ARRIBA, id),
                    movimientos = movimiento,
                    bloqueado = true,
                    fallos = it.fallos + 1,
                    racha = false,
                    rachaTotal = 0,
                    puntuacion = if(it.puntuacion >= 2) it.puntuacion - 2 else it.puntuacion,
                    ultimaJugada = Jugada(TipoJugada.FALLO, movimiento),
                )
            }
            jobFallo = viewModelScope.launch {
                delay(retardoFalloMs) // suspende sin bloquear el hilo principal (nunca Thread.sleep)
                _uiState.update {
                    it.copy(
                        cartas = it.cartas.conEstado(EstadoCarta.BOCA_ABAJO, primera.id, id),
                        bloqueado = false,
                    )
                }
            }
        }
    }

    /** Evento: la app pasa a segundo plano. Lo llama LifecycleResumeEffect (3C.1 §5.3): el tiempo se para. */
    fun onSalirDeLaApp() {
        jobCronometro?.cancel()
    }

    /** Evento: la app vuelve a primer plano. El tiempo sigue solo si la partida está en juego. */
    fun onVolverALaApp() {
        if (_uiState.value.enJuego) arrancarCronometro()
    }

    /**
     * Temporizador: una tarea que se puede parar (3C.1 §5.1). Guardo el Job para poder
     * cancelarlo, y la corrutina vive en viewModelScope, que se cancela sola al destruirse el ViewModel.
     */
    private fun arrancarCronometro() {
        jobCronometro?.cancel() // nunca dos corrutinas contando a la vez
        jobCronometro = viewModelScope.launch {
            while (true) {
                delay(1_000) // suspende sin bloquear el hilo principal
                _uiState.update { it.copy(segundos = it.segundos + 1) }
            }
        }
    }
}

/**
 * Devuelve una lista NUEVA donde solo cambian las cartas con esos ids (map + copy).
 * Las listas son inmutables (apuntes de Kotlin, listas §3): no modifico la que hay, creo otra.
 */
private fun List<Carta>.conEstado(estado: EstadoCarta, vararg ids: Int) =
    map { if (it.id in ids) it.copy(estado = estado) else it }
