package com.pm2627.p1ev.memory

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pm2627.p1ev.memory.datos.NivelesRepositorio
import com.pm2627.p1ev.memory.ui.tablero.TableroScreen
import com.pm2627.p1ev.memory.ui.tablero.TableroViewModel
import com.pm2627.p1ev.memory.ui.theme.MemoryTheme

/**
 * LA ACTIVITY: solo arranca la app.
 *
 * De momento muestra directamente un tablero del nivel Cazador. Cuando haya tres pantallas,
 * aquí se pintará MemoryApp, con la pila de navegación.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MemoryTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val nivel = NivelesRepositorio.nivelesPorDefecto[1]
                    // viewModel { } busca antes de crear: lo crea con parámetros la primera vez
                    // y lo reutiliza después (por ejemplo, al girar el móvil)
                    val viewModel = viewModel { TableroViewModel(nivel) }
                    TableroScreen(
                        nivel = nivel,
                        viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding),
                    )
                }
            }
        }
    }
}