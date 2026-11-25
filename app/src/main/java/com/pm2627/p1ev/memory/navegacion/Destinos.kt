package com.pm2627.p1ev.memory.navegacion

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

/*
 * LOS TRES DESTINOS de Navigation 3 (apuntes 3B.1 §3).
 * Cada destino es una clave @Serializable que implementa NavKey. Las claves solo llevan
 * datos sencillos (textos y números): nunca las cartas ni el ViewModel.
 */

/** Pantalla de inicio: elegir nivel. */
@Serializable
data object Inicio : NavKey

/** El tablero. Solo necesita saber qué nivel se juega. */
@Serializable
data class Tablero(val nivelId: String) : NavKey

/** El resultado de una partida ganada. */
@Serializable
data class Resultado(
    val nivelId: String,
    val movimientos: Int,
    val segundos: Int,
) : NavKey
