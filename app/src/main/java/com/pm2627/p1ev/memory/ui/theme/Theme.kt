package com.pm2627.p1ev.memory.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

// El juego tiene un único tema oscuro (como un libro antiguo a la luz de una vela).
// No uso color dinámico: si no, Android 12+ cambiaría mi paleta por la del fondo de pantalla.
private val EsquemaBestiario = darkColorScheme(
    primary = Sangre,
    onPrimary = TextoClaro,
    primaryContainer = SangreClara,
    secondary = Oro,
    onSecondary = Tinta,
    tertiary = Musgo,
    background = Tinta,
    onBackground = TextoClaro,
    surface = Cuero,
    onSurface = TextoClaro,
    surfaceVariant = CueroClaro,
    onSurfaceVariant = TextoTenue,
    outline = Oro,
)

@Composable
fun MemoryTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = EsquemaBestiario,
        typography = Typography,
        content = content,
    )
}
