package com.pm2627.p1ev.memory.ui.tablero

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pm2627.p1ev.memory.R
import com.pm2627.p1ev.memory.efectos.rememberSonidos
import com.pm2627.p1ev.memory.model.Carta
import com.pm2627.p1ev.memory.model.Criatura
import com.pm2627.p1ev.memory.model.EstadoCarta
import com.pm2627.p1ev.memory.model.Nivel
import com.pm2627.p1ev.memory.ui.componentes.CartaView
import com.pm2627.p1ev.memory.ui.componentes.DURACION_GIRO_MS
import com.pm2627.p1ev.memory.ui.theme.MemoryTheme
import com.pm2627.p1ev.memory.ui.theme.Oro
import kotlinx.coroutines.delay

/**
 * PANTALLA DEL TABLERO, la parte "Stateful" (apuntes 3C.1 §4.1): conoce el ViewModel.
 *
 * - Observa el estado con collectAsStateWithLifecycle(). Cuando llega un estado nuevo,
 *   Compose se recompone: UI = f(estado).
 * - LifecycleResumeEffect (3C.1 §5.3): al salir de la app para el tiempo y al volver lo reanuda.
 * - LaunchedEffect(estado.ultimaJugada): vibración y sonido de cada jugada (mejoras opcionales).
 * - LaunchedEffect(estado.ganada): avisa de la victoria una sola vez para navegar al resultado.
 *
 * Esta pantalla NO decide nada del juego: solo envía eventos al ViewModel (onCartaPulsada(id)).
 */
@Composable
fun TableroScreen(
    nivel: Nivel,
    viewModel: TableroViewModel,
    sonidoActivado: Boolean,
    onVolver: () -> Unit,
    onPartidaGanada: (movimientos: Int, segundos: Int) -> Unit,
) {
    val estado by viewModel.uiState.collectAsStateWithLifecycle()
    val haptica = LocalHapticFeedback.current
    val sonidos = rememberSonidos()

    // Se ejecuta al llegar a RESUMED; onPauseOrDispose, al salir de ese estado
    LifecycleResumeEffect(Unit) {
        viewModel.onVolverALaApp()
        onPauseOrDispose { viewModel.onSalirDeLaApp() }
    }

    // Mejoras: vibración (háptica) y sonido según lo que ha pasado en la jugada
    LaunchedEffect(estado.ultimaJugada) {
        val jugada = estado.ultimaJugada ?: return@LaunchedEffect
        when (jugada.tipo) {
            TipoJugada.PAREJA -> {
                haptica.performHapticFeedback(HapticFeedbackType.Confirm)
                if (sonidoActivado) sonidos.pareja()
            }
            TipoJugada.FALLO -> {
                haptica.performHapticFeedback(HapticFeedbackType.Reject)
                if (sonidoActivado) sonidos.fallo()
            }
            TipoJugada.VICTORIA -> {
                haptica.performHapticFeedback(HapticFeedbackType.LongPress)
                if (sonidoActivado) sonidos.victoria()
            }
        }
    }

    LaunchedEffect(estado.ganada) {
        if (estado.ganada) {
            delay(DURACION_GIRO_MS + 700L) // dejo ver la última pareja antes de cambiar de pantalla
            onPartidaGanada(estado.movimientos, estado.segundos)
        }
    }

    TableroContent(
        nivel = nivel,
        estado = estado,
        onCartaPulsada = { id ->
            if (sonidoActivado && !estado.bloqueado) sonidos.giro()
            viewModel.onCartaPulsada(id)
        },
        onReiniciar = viewModel::onNuevaPartida,
        onVolver = onVolver,
    )
}

/**
 * La parte "Stateless" (3C.1 §4.2): recibe el estado y avisa de los eventos con lambdas
 * (state hoisting). No guarda nada, así que se puede previsualizar con @Preview.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TableroContent(
    nivel: Nivel,
    estado: TableroUiState,
    onCartaPulsada: (Int) -> Unit,
    onReiniciar: () -> Unit,
    onVolver: () -> Unit,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(nivel.nombre, style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(painterResource(R.drawable.ic_volver), contentDescription = "Volver al inicio")
                    }
                },
                actions = {
                    IconButton(onClick = onReiniciar) {
                        Icon(painterResource(R.drawable.ic_reiniciar), contentDescription = "Reiniciar partida")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                ),
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Marcadores(estado)
            Text(
                "Fallos ${estado.fallos} . Puntuación ${estado.puntuacion}",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Cuadricula(nivel, estado.cartas, onCartaPulsada, Modifier.weight(1f))
        }
    }
}

@Composable
private fun Marcadores(estado: TableroUiState) {
    Row(
        modifier = Modifier.fillMaxWidth().widthIn(max = 560.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Marcador("Movimientos", "${estado.movimientos}", Modifier.weight(1f))
        Marcador("Parejas", "${estado.parejasEncontradas}/${estado.parejasTotales}", Modifier.weight(1f))
        Marcador("Tiempo", formatearTiempo(estado.segundos), Modifier.weight(1f))

    }
}

@Composable
private fun Marcador(titulo: String, valor: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surface,
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(1.dp, Oro.copy(alpha = 0.5f)),
    ) {
        Column(Modifier.padding(vertical = 8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(titulo.uppercase(), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(valor, style = MaterialTheme.typography.titleLarge, color = Oro, textAlign = TextAlign.Center)
        }
    }
}

/**
 * La cuadrícula de cartas con LazyVerticalGrid y GridCells.Fixed (apuntes 3A.3 §4.2).
 * En vertical uso las columnas del nivel; en horizontal las giro (más columnas que filas)
 * para que el tablero quepa sin hacer scroll.
 * key = id (3A.3 §3): así Compose sabe qué carta es cuál y solo redibuja la que cambia.
 */
@Composable
private fun Cuadricula(
    nivel: Nivel,
    cartas: List<Carta>,
    onCartaPulsada: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    BoxWithConstraints(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.TopCenter) {
        val horizontal = maxWidth > maxHeight
        val columnas = if (horizontal) maxOf(nivel.filas, nivel.columnas) else minOf(nivel.filas, nivel.columnas)
        val filas = nivel.totalCartas / columnas
        // Ancho máximo para que las filas quepan en el alto disponible (cartas 3:4)
        val anchoPorAlto = (maxHeight - 8.dp * (filas - 1)) / filas * 3f / 4f * columnas + 8.dp * (columnas - 1)
        Box(Modifier.widthIn(max = minOf(maxWidth, anchoPorAlto))) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(columnas),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(cartas, key = { it.id }) { carta ->
                    CartaView(carta = carta, onClick = { onCartaPulsada(carta.id) })
                }
            }
        }
    }
}

fun formatearTiempo(segundos: Int): String = "%d:%02d".format(segundos / 60, segundos % 60)

@Preview(showBackground = true, widthDp = 380, heightDp = 760)
@Composable
private fun TableroPreview() {
    val nivel = Nivel("maestro", "Maestro", filas = 4, columnas = 4)
    val cartas = Criatura.entries.take(8).flatMap { listOf(it, it) }.mapIndexed { i, c ->
        Carta(i, c, if (i < 4) EstadoCarta.EMPAREJADA else if (i == 6) EstadoCarta.BOCA_ARRIBA else EstadoCarta.BOCA_ABAJO)
    }
    MemoryTheme {
        TableroContent(nivel, TableroUiState(cartas = cartas, movimientos = 5, segundos = 42, empezada = true), {}, {}, {})
    }
}
