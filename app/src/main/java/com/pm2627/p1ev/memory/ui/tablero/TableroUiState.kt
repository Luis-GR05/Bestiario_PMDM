package com.pm2627.p1ev.memory.ui.tablero

import com.pm2627.p1ev.memory.model.Carta
import com.pm2627.p1ev.memory.model.EstadoCarta

/**
 * EL ESTADO DE LA PARTIDA (UiState): todo lo que pinta la pantalla del tablero.
 * Es la fuente única de verdad de la pantalla (apuntes 3C.1 §2).
 * Inmutable: data class con val. El ViewModel no lo modifica, crea uno nuevo con copy().
 */
data class TableroUiState(
    val cartas: List<Carta> = emptyList(),
    val movimientos: Int = 0,
    /** true mientras se enseñan dos cartas que no son pareja: no se puede pulsar nada. */
    val bloqueado: Boolean = false,
    val ganada: Boolean = false,
) {
    // Propiedades calculadas (3C.1 §2.1): se sacan de las cartas, así no se pueden desincronizar.
    val parejasTotales: Int get() = cartas.size / 2
    val parejasEncontradas: Int get() = cartas.count { it.emparejada } / 2
    val cartasBocaArriba: List<Carta> get() = cartas.filter { it.estado == EstadoCarta.BOCA_ARRIBA }
}