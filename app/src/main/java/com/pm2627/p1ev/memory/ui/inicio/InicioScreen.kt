package com.pm2627.p1ev.memory.ui.inicio

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pm2627.p1ev.memory.datos.NivelesRepositorio
import com.pm2627.p1ev.memory.model.Nivel
import com.pm2627.p1ev.memory.ui.theme.MemoryTheme

/**
 * PANTALLA DE INICIO: enseña los niveles y avisa de cuál se ha elegido.
 * No guarda estado: los niveles le llegan desde MemoryApp y el clic sube como evento.
 */
@Composable
fun InicioScreen(
    niveles: List<Nivel>,
    onNivelElegido: (Nivel) -> Unit,
) {
    Surface(color = MaterialTheme.colorScheme.background, modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Text("BESTIARIO", style = MaterialTheme.typography.displaySmall)
            Text(
                "Encuentra las parejas de bestias antes de que te devoren.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(4.dp))
            niveles.forEach { nivel ->
                TarjetaNivel(nivel = nivel, onClick = { onNivelElegido(nivel) })
            }
        }
    }
}

@Composable
private fun TarjetaNivel(nivel: Nivel, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.widthIn(max = 480.dp).fillMaxWidth(),
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(nivel.nombre, style = MaterialTheme.typography.titleLarge)
                Text(nivel.descripcion, style = MaterialTheme.typography.bodyMedium)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("${nivel.filas} × ${nivel.columnas}", style = MaterialTheme.typography.titleMedium)
                Text("${nivel.parejas} parejas", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 380, heightDp = 800)
@Composable
private fun InicioPreview() {
    MemoryTheme {
        InicioScreen(NivelesRepositorio.nivelesPorDefecto, {})
    }
}