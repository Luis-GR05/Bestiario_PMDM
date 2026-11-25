package com.pm2627.p1ev.memory.datos

import org.junit.Assert.assertEquals
import org.junit.Test

class NivelesRepositorioTest {

    @Test
    fun `parsea los niveles del json, con campos opcionales`() {
        val texto = """
            [
              {"id": "a", "nombre": "Aprendiz", "filas": 3, "columnas": 2},
              {"id": "p", "nombre": "Pesadilla", "filas": 4, "columnas": 4, "retardoFalloMs": 450, "campoNuevo": true}
            ]
        """.trimIndent()

        val niveles = NivelesRepositorio.parsear(texto)

        assertEquals(2, niveles.size)
        assertEquals(3, niveles[0].parejas)
        assertEquals(900L, niveles[0].retardoFalloMs) // valor por defecto
        assertEquals(450L, niveles[1].retardoFalloMs)
    }

    @Test
    fun `los niveles por defecto son al menos tres y de tamano creciente`() {
        val niveles = NivelesRepositorio.nivelesPorDefecto
        assertEquals(true, niveles.size >= 3)
        assertEquals(niveles.map { it.totalCartas }.sorted(), niveles.map { it.totalCartas })
    }
}
