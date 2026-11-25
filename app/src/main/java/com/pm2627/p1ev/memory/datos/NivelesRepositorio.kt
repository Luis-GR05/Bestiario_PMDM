package com.pm2627.p1ev.memory.datos

import android.content.Context
import com.pm2627.p1ev.memory.model.Nivel
import kotlinx.serialization.json.Json

/**
 * Lee los niveles del fichero assets/niveles.json (mejora opcional).
 *
 * La lectura del fichero (cargar) está separada de la conversión del texto (parsear)
 * para poder probar parsear() en un test de JVM sin Context.
 */
object NivelesRepositorio {

    private const val FICHERO = "niveles.json"

    /** Si el JSON falta o está mal escrito, el juego sigue funcionando con estos. */
    val nivelesPorDefecto = listOf(
        Nivel(id = "aprendiz", nombre = "Aprendiz", descripcion = "3 parejas", filas = 3, columnas = 2),
        Nivel(id = "cazador", nombre = "Cazador", descripcion = "6 parejas", filas = 4, columnas = 3),
        Nivel(id = "maestro", nombre = "Maestro", descripcion = "8 parejas", filas = 4, columnas = 4),
    )

    // ignoreUnknownKeys: si añado campos nuevos al JSON, las versiones viejas no fallan
    private val json = Json { ignoreUnknownKeys = true }

    fun parsear(texto: String): List<Nivel> = json.decodeFromString(texto)

    fun cargar(context: Context): List<Nivel> =
        runCatching {
            val texto = context.assets.open(FICHERO).bufferedReader().use { it.readText() }
            parsear(texto)
        }.getOrNull()?.takeIf { it.isNotEmpty() } ?: nivelesPorDefecto
}
