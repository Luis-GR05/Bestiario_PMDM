package com.pm2627.p1ev.memory.ui.tablero

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pm2627.p1ev.memory.datos.NivelesRepositorio
import com.pm2627.p1ev.memory.model.Carta
import com.pm2627.p1ev.memory.model.EstadoCarta
import com.pm2627.p1ev.memory.model.repartir
import com.pm2627.p1ev.memory.ui.theme.MemoryTheme
import kotlin.random.Random

/**
 * LA PANTALLA DEL TABLERO · VERSIÓN PROVISIONAL
 *
 * Ya usa el modelo nuevo: reparte las cartas del primer nivel con repartir() y, al tocar
 * una casilla, cambia su estado entre BOCA_ABAJO y BOCA_ARRIBA. Todavía no hay reglas.
 *
 * Sigue estando MAL a propósito: el estado vive aquí, con remember, y se pierde al girar
 * el móvil. En la siguiente parte lo llevo al TableroViewModel.
 *
 * Apuntes: Compose · estado (22-state-management) · 3C.1 §1.1 (cambios de configuración)
 */
@Composable
fun TableroScreen(modifier: Modifier = Modifier) {
    // ⚠️ El estado de la partida, dentro de la pantalla: se pierde al girar el móvil.
    var cartas by remember { mutableStateOf(repartir(NivelesRepositorio.nivelesPorDefecto.first(), Random.Default)) }
    var toques by remember { mutableIntStateOf(0) }

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text("Toques: $toques", style = MaterialTheme.typography.titleLarge)

        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 100.dp),
            modifier = Modifier.widthIn(max = 720.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(cartas) { carta ->
                Casilla(
                    carta = carta,
                    onClick = {
                        // La lista es inmutable: se crea una lista NUEVA con esa carta cambiada.
                        cartas = cartas.map {
                            if (it.id == carta.id) {
                                it.copy(estado = if (it.bocaAbajo) EstadoCarta.BOCA_ARRIBA else EstadoCarta.BOCA_ABAJO)
                            } else {
                                it
                            }
                        }
                        toques++
                    },
                )
            }
        }
    }
}

/** Una casilla lisa: «?» boca abajo y el nombre de su criatura boca arriba. */
@Composable
private fun Casilla(carta: Carta, onClick: () -> Unit) {
    Card(
        modifier = Modifier.aspectRatio(3f / 4f).clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = if (!carta.bocaAbajo) MaterialTheme.colorScheme.secondaryContainer
            else MaterialTheme.colorScheme.primary,
        ),
    ) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = if (!carta.bocaAbajo) carta.criatura.nombre else "?",
                style = MaterialTheme.typography.titleMedium,
                color = if (!carta.bocaAbajo) MaterialTheme.colorScheme.onSecondaryContainer
                else MaterialTheme.colorScheme.onPrimary,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TableroScreenPreview() {
    MemoryTheme {
        TableroScreen()
    }
}