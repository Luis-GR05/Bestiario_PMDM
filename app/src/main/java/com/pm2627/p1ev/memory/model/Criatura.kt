package com.pm2627.p1ev.memory.model

import androidx.annotation.DrawableRes
import com.pm2627.p1ev.memory.R

/** Las cartas se dividen en criaturas de leyenda y animales reales. */
enum class TipoCriatura(val etiqueta: String) {
    MITICA("Mítica"),
    ANIMAL("Animal"),
}

/**
 * Cada dibujo posible de una carta (enum con propiedades, apuntes de Kotlin 24 §2).
 *
 * La imagen es un grabado antiguo de dominio público (ver README, apartado 2).
 * Con la criatura basta para saber qué imagen pintar y si dos cartas son pareja.
 * El orden importa: los niveles pequeños usan las primeras criaturas de la lista.
 */
enum class Criatura(
    val nombre: String,
    val tipo: TipoCriatura,
    @param:DrawableRes val imagen: Int,
) {
    HIDRA("Hidra", TipoCriatura.MITICA, R.drawable.carta_hidra),
    LOBO("Lobo", TipoCriatura.ANIMAL, R.drawable.carta_lobo),
    KRAKEN("Kraken", TipoCriatura.MITICA, R.drawable.carta_kraken),
    RAPE("Rape abisal", TipoCriatura.ANIMAL, R.drawable.carta_rape),
    ARPIA("Arpía", TipoCriatura.MITICA, R.drawable.carta_arpia),
    MURCIELAGO("Murciélago", TipoCriatura.ANIMAL, R.drawable.carta_murcielago),
    BASILISCO("Basilisco", TipoCriatura.MITICA, R.drawable.carta_basilisco),
    HIENA("Hiena", TipoCriatura.ANIMAL, R.drawable.carta_hiena),
}