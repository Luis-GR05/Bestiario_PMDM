package com.pm2627.p1ev.memory

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.pm2627.p1ev.memory.navegacion.MemoryApp
import com.pm2627.p1ev.memory.ui.theme.MemoryTheme

/**
 * LA ACTIVITY: solo arranca la app con el tema y la navegación (MemoryApp).
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MemoryTheme {
                MemoryApp()
            }
        }
    }
}