package com.pm2627.p1ev.memory.datos

import com.pm2627.p1ev.memory.model.Nivel

/**
 * De dónde salen los niveles del juego.
 * De momento están escritos aquí, en el código.
 */
object NivelesRepositorio {

    val nivelesPorDefecto = listOf(
        Nivel(id = "aprendiz", nombre = "Aprendiz", descripcion = "3 parejas", filas = 3, columnas = 2),
        Nivel(id = "cazador", nombre = "Cazador", descripcion = "6 parejas", filas = 4, columnas = 3),
        Nivel(id = "maestro", nombre = "Maestro", descripcion = "8 parejas", filas = 4, columnas = 4),
    )
}