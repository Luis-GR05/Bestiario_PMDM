package com.pmdm.memoria.ui.modo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
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

/*
 * TAREA 6 · Pantalla de modo de juego
 *
 * Esta pantalla solo contiene la interfaz: no sabe nada de navegación, de tu
 * ViewModel ni de tu tablero. Lo que tienes que hacer tú:
 *
 *  1. Ajustar el nombre del paquete de este fichero al de tus pantallas, si hace falta.
 *
 *  2. Crear el destino de navegación de esta pantalla y colocarlo entre Inicio y Tablero:
 *     - Inicio lleva a esta pantalla cuando se elige el nivel.
 *     - Esta pantalla lleva al Tablero cuando se elige el modo (callback onModoElegido).
 *
 *  3. Pasar el modo elegido como dato sencillo (aquí, un Boolean: true = «Con pista»)
 *     hasta el Tablero y su ViewModel, y guardarlo en el estado de la pantalla.
 *     No hay que implementar la pista: solo propagar el modo.
 *
 *  4. Decidir a dónde lleva «atrás» desde esta pantalla y desde el Tablero,
 *     y dejar un comentario que explique por qué.
 */

private const val TITULO = "Elige el modo de juego"
private const val NORMAL = "Normal"
private const val NORMAL_DETALLE = "Juegas sin ayudas"
private const val CON_PISTA = "Con pista"
private const val CON_PISTA_DETALLE = "Podrás recibir ayuda durante la partida"

@Composable
fun ModoScreen(
    onModoElegido: (conPista: Boolean) -> Unit,
    modifier: Modifier = Modifier,
    nombreNivel: String = ""
) {
    Surface(modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.safeDrawingPadding().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(TITULO, style = MaterialTheme.typography.headlineMedium)
            if (nombreNivel.isNotEmpty()) {
                Text(
                    nombreNivel,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(Modifier.height(32.dp))

            Button(onClick = { onModoElegido(false) }, modifier = Modifier.fillMaxWidth()) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(NORMAL, style = MaterialTheme.typography.titleMedium)
                    Text(NORMAL_DETALLE, style = MaterialTheme.typography.bodySmall, textAlign = TextAlign.Center)
                }
            }
            Spacer(Modifier.height(12.dp))
            OutlinedButton(onClick = { onModoElegido(true) }, modifier = Modifier.fillMaxWidth()) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(CON_PISTA, style = MaterialTheme.typography.titleMedium)
                    Text(CON_PISTA_DETALLE, style = MaterialTheme.typography.bodySmall, textAlign = TextAlign.Center)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ModoScreenPreview() {
    MaterialTheme { ModoScreen(onModoElegido = {}, nombreNivel = "Nivel fácil") }
}
