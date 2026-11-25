package com.pm2627.p1ev.memory.ui.tablero

import com.pm2627.p1ev.memory.model.EstadoCarta
import com.pm2627.p1ev.memory.model.Nivel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import kotlin.random.Random

/**
 * Tests del TableroViewModel (apuntes 3C.4 §4 y 3C.1 §5.4).
 * Cada test sigue el patrón Arrange (preparo), Act (pulso) y Assert (compruebo).
 * Uso Random(42) para que el reparto sea siempre el mismo, y los ids de las parejas los
 * saco del estado con groupBy, sin memorizar posiciones.
 * El temporizador SIEMPRE se para al final (finally): ¡páralo siempre!
 */
@OptIn(ExperimentalCoroutinesApi::class)
class TableroViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val nivel = Nivel(id = "test", nombre = "Test", filas = 2, columnas = 3) // 3 parejas
    private val retardo = 900L

    private fun crearViewModel() = TableroViewModel(nivel, Random(42), retardo)

    /** Lista de parejas: cada elemento son los dos ids de una misma criatura. */
    private fun TableroViewModel.parejas(): List<List<Int>> =
        uiState.value.cartas.groupBy { it.criatura }.values.map { cartas -> cartas.map { it.id } }

    /** Dos ids que NO son pareja. */
    private fun TableroViewModel.noPareja(): Pair<Int, Int> {
        val p = parejas()
        return p[0][0] to p[1][0]
    }

    private fun TableroViewModel.estadoDe(id: Int) = uiState.value.cartas.first { it.id == id }.estado

    private suspend fun TestScope.probar(vm: TableroViewModel, bloque: suspend TestScope.() -> Unit) {
        try {
            bloque()
        } finally {
            vm.onSalirDeLaApp() // ¡páralo siempre!
            runCurrent()
        }
    }

    @Test
    fun `al empezar todas las cartas estan boca abajo y hay una pareja de cada criatura`() {
        val vm = crearViewModel()
        val estado = vm.uiState.value

        assertEquals(6, estado.cartas.size)
        assertTrue(estado.cartas.all { it.estado == EstadoCarta.BOCA_ABAJO })
        assertTrue(vm.parejas().all { it.size == 2 })
        assertEquals(0, estado.movimientos)
    }

    @Test
    fun `emparejar dos cartas iguales las deja emparejadas y cuenta un movimiento`() = runTest {
        val vm = crearViewModel()
        probar(vm) {
            val (a, b) = vm.parejas()[0]

            vm.onCartaPulsada(a)
            vm.onCartaPulsada(b)

            assertEquals(EstadoCarta.EMPAREJADA, vm.estadoDe(a))
            assertEquals(EstadoCarta.EMPAREJADA, vm.estadoDe(b))
            assertEquals(1, vm.uiState.value.movimientos)
            assertEquals(1, vm.uiState.value.parejasEncontradas)
            assertEquals(TipoJugada.PAREJA, vm.uiState.value.ultimaJugada?.tipo)
        }
    }

    @Test
    fun `fallar bloquea el tablero y tras el retardo las cartas vuelven boca abajo`() = runTest {
        val vm = crearViewModel()
        probar(vm) {
            val (a, b) = vm.noPareja()

            vm.onCartaPulsada(a)
            vm.onCartaPulsada(b)

            // Justo después del fallo: las dos boca arriba y el tablero bloqueado
            assertEquals(EstadoCarta.BOCA_ARRIBA, vm.estadoDe(a))
            assertEquals(EstadoCarta.BOCA_ARRIBA, vm.estadoDe(b))
            assertTrue(vm.uiState.value.bloqueado)
            assertEquals(1, vm.uiState.value.movimientos)

            // Mientras está bloqueado, pulsar otra carta no hace nada
            val otra = vm.parejas()[2][0]
            vm.onCartaPulsada(otra)
            assertEquals(EstadoCarta.BOCA_ABAJO, vm.estadoDe(otra))

            advanceTimeBy(retardo) // avanza el reloj virtual el tiempo del retardo
            runCurrent()           // ejecuta también lo programado justo en ese instante

            assertEquals(EstadoCarta.BOCA_ABAJO, vm.estadoDe(a))
            assertEquals(EstadoCarta.BOCA_ABAJO, vm.estadoDe(b))
            assertFalse(vm.uiState.value.bloqueado)
            assertEquals(1, vm.uiState.value.movimientos)
        }
    }

    @Test
    fun `encontrar todas las parejas gana la partida y para el tiempo`() = runTest {
        val vm = crearViewModel()
        probar(vm) {
            vm.parejas().forEach { (a, b) ->
                vm.onCartaPulsada(a)
                vm.onCartaPulsada(b)
            }

            val estado = vm.uiState.value
            assertTrue(estado.ganada)
            assertEquals(3, estado.movimientos)
            assertEquals(3, estado.parejasEncontradas)
            assertEquals(TipoJugada.VICTORIA, estado.ultimaJugada?.tipo)

            // El tiempo ya no avanza
            val segundos = estado.segundos
            advanceTimeBy(5_000)
            runCurrent()
            assertEquals(segundos, vm.uiState.value.segundos)
        }
    }

    @Test
    fun `pulsar una carta ya girada o emparejada no hace nada`() = runTest {
        val vm = crearViewModel()
        probar(vm) {
            val (a, b) = vm.parejas()[0]
            vm.onCartaPulsada(a)
            vm.onCartaPulsada(a) // la misma otra vez: no cuenta como segunda carta
            assertEquals(0, vm.uiState.value.movimientos)
            assertEquals(EstadoCarta.BOCA_ARRIBA, vm.estadoDe(a))

            vm.onCartaPulsada(b)
            vm.onCartaPulsada(a) // ya emparejada
            vm.onCartaPulsada(99) // no existe
            assertEquals(1, vm.uiState.value.movimientos)
        }
    }

    @Test
    fun `el tiempo empieza con la primera carta y cuenta segundos`() = runTest {
        val vm = crearViewModel()
        probar(vm) {
            advanceTimeBy(3_000)
            runCurrent()
            assertEquals(0, vm.uiState.value.segundos) // aún no se ha pulsado nada

            vm.onCartaPulsada(vm.parejas()[0][0])
            advanceTimeBy(3_000)
            runCurrent()
            assertEquals(3, vm.uiState.value.segundos)
        }
    }

    @Test
    fun `salir de la app pausa el tiempo y volver no cuenta el doble`() = runTest {
        val vm = crearViewModel()
        probar(vm) {
            vm.onCartaPulsada(vm.parejas()[0][0])
            advanceTimeBy(2_000)
            runCurrent()

            vm.onSalirDeLaApp()
            advanceTimeBy(10_000)
            runCurrent()
            assertEquals(2, vm.uiState.value.segundos) // en pausa no corre

            vm.onVolverALaApp()
            vm.onVolverALaApp() // dos veces seguidas: sigue habiendo un solo cronómetro
            advanceTimeBy(3_000)
            runCurrent()
            assertEquals(5, vm.uiState.value.segundos)
        }
    }

    @Test
    fun `nueva partida reinicia contadores y vuelve a repartir`() = runTest {
        val vm = crearViewModel()
        probar(vm) {
            val (a, b) = vm.parejas()[0]
            vm.onCartaPulsada(a)
            vm.onCartaPulsada(b)

            vm.onNuevaPartida()

            val estado = vm.uiState.value
            assertEquals(0, estado.movimientos)
            assertEquals(0, estado.segundos)
            assertFalse(estado.empezada)
            assertTrue(estado.cartas.all { it.estado == EstadoCarta.BOCA_ABAJO })
        }
    }
}
