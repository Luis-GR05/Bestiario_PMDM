package com.pm2627.p1ev.memory.model

/**
 * Un nivel del juego. Todos los datos del tablero salen de aquí, no están repartidos
 * por el código: filas y columnas, y de ahí se calculan las cartas y las parejas.
 */
data class Nivel(
    val id: String,
    val nombre: String,
    val descripcion: String = "",
    val filas: Int,
    val columnas: Int,
) {
    val totalCartas: Int get() = filas * columnas
    val parejas: Int get() = totalCartas / 2

    init {
        require(totalCartas % 2 == 0) { "El nivel $id tiene un número impar de cartas" }
        require(parejas <= Criatura.entries.size) { "El nivel $id necesita más criaturas de las que hay" }
    }
}