package com.pm2627.p1ev.memory.ui.inicio

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pm2627.p1ev.memory.R
import com.pm2627.p1ev.memory.datos.NivelesRepositorio
import com.pm2627.p1ev.memory.model.Criatura
import com.pm2627.p1ev.memory.model.Nivel
import com.pm2627.p1ev.memory.ui.theme.MemoryTheme
import com.pm2627.p1ev.memory.ui.theme.Oro

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
            Portada()
            Text("BESTIARIO", style = MaterialTheme.typography.displaySmall, color = Oro)
            Text(
                "Encuentra las parejas de bestias antes de que te devoren.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(4.dp))
            niveles.forEach { nivel ->
                TarjetaNivel(nivel = nivel, onClick = { onNivelElegido(nivel) })
            }
        }
    }
}

/** Tres cartas en abanico como portada: el reverso y dos grabados. */
@Composable
private fun Portada() {
    Row(horizontalArrangement = Arrangement.spacedBy((-28).dp), verticalAlignment = Alignment.CenterVertically) {
        MiniCarta(Criatura.KRAKEN.imagen, -12f)
        MiniCarta(R.drawable.carta_reverso, 0f, grande = true)
        MiniCarta(Criatura.HIDRA.imagen, 12f)
    }
}

@Composable
private fun MiniCarta(imagen: Int, giro: Float, grande: Boolean = false) {
    val ancho = if (grande) 110.dp else 92.dp
    Image(
        painter = painterResource(imagen),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .rotate(giro)
            .size(width = ancho, height = ancho * 4f / 3f)
            .clip(RoundedCornerShape(10.dp)),
    )
}

@Composable
private fun TarjetaNivel(nivel: Nivel, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.widthIn(max = 480.dp).fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, Oro.copy(alpha = 0.6f)),
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(nivel.nombre, style = MaterialTheme.typography.titleLarge, color = Oro)
                Text(nivel.descripcion, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("${nivel.filas} × ${nivel.columnas}", style = MaterialTheme.typography.titleMedium)
                Text("${nivel.parejas} parejas", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
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
