package com.pm2627.p1ev.memory.model

import kotlinx.serialization.Serializable

/**
 * Un nivel del juego. Todos los datos del tablero salen de aquí, no están repartidos
 * por el código: para añadir un nivel solo hay que añadir una entrada en
 * assets/niveles.json (mejora opcional "niveles en JSON").
 *
 * Es @Serializable para poder leerlo del JSON con kotlinx.serialization.
 */
@Serializable
data class Nivel(
    val id: String,
    val nombre: String,
    val descripcion: String = "",
    val filas: Int,
    val columnas: Int,
    /** Cuánto tiempo se enseñan dos cartas que no son pareja antes de girarlas. */
    val retardoFalloMs: Long = 900,
) {
    val totalCartas: Int get() = filas * columnas
    val parejas: Int get() = totalCartas / 2

    init {
        require(totalCartas % 2 == 0) { "El nivel $id tiene un número impar de cartas" }
        require(parejas <= Criatura.entries.size) { "El nivel $id necesita más criaturas de las que hay" }
    }
}
