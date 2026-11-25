package com.pm2627.p1ev.memory.ui.resultado

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.unit.sp
import com.pm2627.p1ev.memory.model.Nivel
import com.pm2627.p1ev.memory.model.calcularEstrellas
import com.pm2627.p1ev.memory.ui.tablero.formatearTiempo
import com.pm2627.p1ev.memory.ui.theme.MemoryTheme
import com.pm2627.p1ev.memory.ui.theme.Oro
import com.pm2627.p1ev.memory.ui.theme.Sangre
import com.pm2627.p1ev.memory.ui.theme.TextoTenue

/**
 * PANTALLA DE RESULTADO: recibe solo números (movimientos y segundos) y el nivel.
 */
@Composable
fun ResultadoScreen(
    nivel: Nivel,
    movimientos: Int,
    segundos: Int,
    onJugarOtraVez: () -> Unit,
    onElegirNivel: () -> Unit,
) {
    val estrellas = calcularEstrellas(movimientos, nivel.parejas)
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
            Text("BESTIARIO", style = MaterialTheme.typography.labelSmall, color = TextoTenue)
            Text(
                "Has sobrevivido",
                style = MaterialTheme.typography.headlineMedium,
                color = Oro,
                textAlign = TextAlign.Center,
            )
            Text("Nivel ${nivel.nombre}", style = MaterialTheme.typography.titleMedium)
            Text(
                text = "★".repeat(estrellas) + "☆".repeat(3 - estrellas),
                fontSize = 44.sp,
                color = Oro,
                modifier = Modifier.padding(vertical = 4.dp),
            )
            Row(
                modifier = Modifier.widthIn(max = 420.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Dato("Movimientos", "$movimientos", Modifier.weight(1f))
                Dato("Tiempo", formatearTiempo(segundos), Modifier.weight(1f))
            }
            Button(
                onClick = onJugarOtraVez,
                colors = ButtonDefaults.buttonColors(containerColor = Sangre),
                modifier = Modifier.widthIn(max = 420.dp).fillMaxWidth(),
            ) { Text("Jugar otra vez", style = MaterialTheme.typography.titleMedium) }
            OutlinedButton(
                onClick = onElegirNivel,
                border = BorderStroke(1.dp, Oro),
                modifier = Modifier.widthIn(max = 420.dp).fillMaxWidth(),
            ) { Text("Elegir nivel", style = MaterialTheme.typography.titleMedium, color = Oro) }
        }
    }
}

@Composable
private fun Dato(titulo: String, valor: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surface,
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(1.dp, Oro.copy(alpha = 0.5f)),
    ) {
        Column(Modifier.padding(vertical = 14.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(titulo.uppercase(), style = MaterialTheme.typography.labelSmall, color = TextoTenue)
            Text(valor, style = MaterialTheme.typography.headlineMedium, color = Oro)
        }
    }
}

@Preview(showBackground = true, widthDp = 380, heightDp = 760)
@Composable
private fun ResultadoPreview() {
    MemoryTheme {
        ResultadoScreen(Nivel("maestro", "Maestro", filas = 4, columnas = 4), 11, 73, {}, {})
    }
}
