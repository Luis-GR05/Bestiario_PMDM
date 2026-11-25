package com.pm2627.p1ev.memory.ui.resultado

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pm2627.p1ev.memory.model.Nivel
import com.pm2627.p1ev.memory.ui.tablero.formatearTiempo
import com.pm2627.p1ev.memory.ui.theme.MemoryTheme

/**
 * PANTALLA DE RESULTADO: recibe solo números y el nivel, y avisa de lo que quiere hacer el jugador.
 */
@Composable
fun ResultadoScreen(
    nivel: Nivel,
    movimientos: Int,
    segundos: Int,
    onJugarOtraVez: () -> Unit,
    onElegirNivel: () -> Unit,
) {
    Surface(color = MaterialTheme.colorScheme.background, modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        ) {
            Text("Has sobrevivido", style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center)
            Text("Nivel ${nivel.nombre}", style = MaterialTheme.typography.titleMedium)
            Text("Movimientos: $movimientos", style = MaterialTheme.typography.titleLarge)
            Text("Tiempo: ${formatearTiempo(segundos)}", style = MaterialTheme.typography.titleLarge)
            Button(
                onClick = onJugarOtraVez,
                modifier = Modifier.widthIn(max = 420.dp).fillMaxWidth(),
            ) { Text("Jugar otra vez") }
            OutlinedButton(
                onClick = onElegirNivel,
                modifier = Modifier.widthIn(max = 420.dp).fillMaxWidth(),
            ) { Text("Elegir nivel") }
        }
    }
}

@Preview(showBackground = true, widthDp = 380, heightDp = 760)
@Composable
private fun ResultadoPreview() {
    MemoryTheme {
        ResultadoScreen(Nivel("maestro", "Maestro", filas = 4, columnas = 4), 11, 83, {}, {})
    }
}
