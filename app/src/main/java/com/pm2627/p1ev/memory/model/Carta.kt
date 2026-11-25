package com.pm2627.p1ev.memory.model

/**
 * Situación de una carta en el tablero (enum class, apuntes de Kotlin 24).
 *
 * Uso un enum porque una carta tiene TRES estados y un Boolean solo da para dos:
 * boca abajo, boca arriba (girada en este turno) y emparejada (ya resuelta).
 */
enum class EstadoCarta {
    BOCA_ABAJO,
    BOCA_ARRIBA,
    EMPAREJADA,
}

/**
 * Una carta del tablero (data class, apuntes de Kotlin 23).
 * Es inmutable: para cambiarla se crea otra con copy(...).
 *
 * - id: identifica la carta en el tablero (es también la key de la cuadrícula).
 * - criatura: el dibujo que enseña. Dos cartas son pareja si tienen la misma criatura.
 * - estado: si está boca abajo, boca arriba o emparejada.
 */
data class Carta(
    val id: Int,
    val criatura: Criatura,
    val estado: EstadoCarta = EstadoCarta.BOCA_ABAJO,
) {
    val bocaAbajo: Boolean get() = estado == EstadoCarta.BOCA_ABAJO
    val emparejada: Boolean get() = estado == EstadoCarta.EMPAREJADA

    /** Dos cartas distintas forman pareja cuando enseñan la misma criatura. */
    fun esParejaDe(otra: Carta): Boolean = id != otra.id && criatura == otra.criatura
}