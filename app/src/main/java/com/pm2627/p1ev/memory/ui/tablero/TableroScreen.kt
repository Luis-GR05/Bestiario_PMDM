package com.pm2627.p1ev.memory.ui.tablero

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pm2627.p1ev.memory.model.Carta
import com.pm2627.p1ev.memory.model.Criatura
import com.pm2627.p1ev.memory.model.EstadoCarta
import com.pm2627.p1ev.memory.model.Nivel
import com.pm2627.p1ev.memory.ui.componentes.CartaView
import com.pm2627.p1ev.memory.ui.theme.MemoryTheme

/**
 * PANTALLA DEL TABLERO, la parte "Stateful" (apuntes 3C.1 §4.1): conoce el ViewModel.
 *
 * Observa el estado con collectAsStateWithLifecycle(). Cuando llega un estado nuevo,
 * Compose se recompone: UI = f(estado).
 *
 * Esta pantalla NO decide nada del juego: solo envía eventos al ViewModel (onCartaPulsada(id)).
 */
@Composable
fun TableroScreen(
    nivel: Nivel,
    viewModel: TableroViewModel,
    modifier: Modifier = Modifier,
) {
    val estado by viewModel.uiState.collectAsStateWithLifecycle()

    TableroContent(
        nivel = nivel,
        estado = estado,
        onCartaPulsada = viewModel::onCartaPulsada,
        onReiniciar = viewModel::onNuevaPartida,
        modifier = modifier,
    )
}

/**
 * La parte "Stateless" (3C.1 §4.2): recibe el estado y avisa de los eventos con lambdas
 * (state hoisting). No guarda nada, así que se puede previsualizar con @Preview.
 */
@Composable
fun TableroContent(
    nivel: Nivel,
    estado: TableroUiState,
    onCartaPulsada: (Int) -> Unit,
    onReiniciar: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(nivel.nombre, style = MaterialTheme.typography.headlineSmall)
        Marcadores(estado)
        if (estado.ganada) {
            Text("¡Has encontrado todas las parejas!", style = MaterialTheme.typography.titleMedium)
        }
        OutlinedButton(onClick = onReiniciar) { Text("Reiniciar") }
        Cuadricula(nivel, estado.cartas, onCartaPulsada)
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
    }
}

@Composable
private fun Marcador(titulo: String, valor: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = MaterialTheme.shapes.medium,
    ) {
        Column(Modifier.padding(vertical = 8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(titulo, style = MaterialTheme.typography.labelMedium)
            Text(valor, style = MaterialTheme.typography.titleLarge, textAlign = TextAlign.Center)
        }
    }
}

/**
 * La cuadrícula de cartas con LazyVerticalGrid y GridCells.Fixed (apuntes 3A.3 §4.2).
 * key = id (3A.3 §3): así Compose sabe qué carta es cuál y solo redibuja la que cambia.
 */
@Composable
private fun Cuadricula(
    nivel: Nivel,
    cartas: List<Carta>,
    onCartaPulsada: (Int) -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(nivel.columnas),
        modifier = Modifier.widthIn(max = 560.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(cartas, key = { it.id }) { carta ->
            CartaView(carta = carta, onClick = { onCartaPulsada(carta.id) })
        }
    }
}

@Preview(showBackground = true, widthDp = 380, heightDp = 760)
@Composable
private fun TableroPreview() {
    val nivel = Nivel("cazador", "Cazador", filas = 4, columnas = 3)
    val cartas = Criatura.entries.take(6).flatMap { listOf(it, it) }.mapIndexed { i, c ->
        Carta(i, c, if (i < 4) EstadoCarta.EMPAREJADA else if (i == 6) EstadoCarta.BOCA_ARRIBA else EstadoCarta.BOCA_ABAJO)
    }
    MemoryTheme {
        TableroContent(nivel, TableroUiState(cartas = cartas, movimientos = 5), {}, {})
    }
}