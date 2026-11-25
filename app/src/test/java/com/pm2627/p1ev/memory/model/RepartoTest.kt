package com.pm2627.p1ev.memory.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.random.Random

/**
 * Tests del reparto. Patrón Arrange (preparo), Act (reparto) y Assert (compruebo).
 */
class RepartoTest {

    private val maestro = Nivel(id = "maestro", nombre = "Maestro", filas = 4, columnas = 4)

    @Test
    fun `cada criatura aparece exactamente dos veces`() {
        val cartas = repartir(maestro, Random(1))

        assertEquals(16, cartas.size)
        assertTrue(cartas.groupBy { it.criatura }.values.all { it.size == 2 })
        assertEquals(16, cartas.map { it.id }.toSet().size) // ids únicos
    }

    @Test
    fun `con la misma semilla el reparto es identico y con otra cambia`() {
        val a = repartir(maestro, Random(7)).map { it.criatura }
        val b = repartir(maestro, Random(7)).map { it.criatura }
        val c = repartir(maestro, Random(8)).map { it.criatura }

        assertEquals(a, b)
        assertNotEquals(a, c)
    }

    @Test
    fun `todas las cartas empiezan boca abajo`() {
        val cartas = repartir(maestro, Random(3))

        assertTrue(cartas.all { it.estado == EstadoCarta.BOCA_ABAJO })
    }

    @Test
    fun `las estrellas premian hacer pocos movimientos`() {
        assertEquals(3, calcularEstrellas(movimientos = 8, parejas = 8))
        assertEquals(2, calcularEstrellas(movimientos = 15, parejas = 8))
        assertEquals(1, calcularEstrellas(movimientos = 30, parejas = 8))
    }
}
