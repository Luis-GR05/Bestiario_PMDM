package com.pm2627.p1ev.memory.model

import kotlin.random.Random

/**
 * Reparte las cartas de un nivel con operaciones funcionales de listas (apuntes de Kotlin 42 §4):
 *   1. take: me quedo con tantas criaturas como parejas tiene el nivel.
 *   2. flatMap: cada criatura aparece dos veces (la pareja).
 *   3. shuffled(random): las barajo. En la app uso Random.Default (orden distinto en
 *      cada partida) y en los tests Random(semilla), para que el orden sea siempre el mismo.
 *   4. mapIndexed: le doy a cada carta un id único según su posición.
 */
fun repartir(
    nivel: Nivel,
    random: Random,
    criaturas: List<Criatura> = Criatura.entries,
): List<Carta> =
    criaturas
        .take(nivel.parejas)
        .flatMap { listOf(it, it) }
        .shuffled(random)
        .mapIndexed { indice, criatura -> Carta(id = indice, criatura = criatura) }

/**
 * Valoración de 1 a 3 estrellas para la pantalla de resultado.
 * Lo ideal sería una jugada por pareja: cuantos más movimientos de sobra, menos estrellas.
 */
fun calcularEstrellas(movimientos: Int, parejas: Int): Int = when {
    movimientos <= parejas + parejas / 2 -> 3
    movimientos <= parejas * 2 -> 2
    else -> 1
}
