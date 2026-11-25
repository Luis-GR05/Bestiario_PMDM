package com.pm2627.p1ev.memory.efectos

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.pm2627.p1ev.memory.R

/**
 * Mejora opcional: sonidos con SoundPool.
 *
 * SoundPool carga los efectos cortos en memoria una vez (load) y luego los reproduce
 * sin retraso (play). Es mejor que MediaPlayer para sonidos de menos de un segundo.
 * Al terminar hay que liberar los recursos con release().
 */
class Sonidos(context: Context) {

    private val pool: SoundPool = SoundPool.Builder()
        .setMaxStreams(3)
        .setAudioAttributes(
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build(),
        )
        .build()

    // load() devuelve un id de sonido que luego uso en play()
    private val idGiro = pool.load(context, R.raw.giro, 1)
    private val idPareja = pool.load(context, R.raw.pareja, 1)
    private val idFallo = pool.load(context, R.raw.fallo, 1)
    private val idVictoria = pool.load(context, R.raw.victoria, 1)

    fun giro() = reproducir(idGiro, volumen = 0.6f)
    fun pareja() = reproducir(idPareja)
    fun fallo() = reproducir(idFallo)
    fun victoria() = reproducir(idVictoria)

    private fun reproducir(id: Int, volumen: Float = 1f) {
        // play(id, volumenIzq, volumenDer, prioridad, repeticiones, velocidad)
        pool.play(id, volumen, volumen, 1, 0, 1f)
    }

    fun liberar() = pool.release()
}

/**
 * Crea los sonidos una vez para la pantalla y los libera cuando la pantalla desaparece.
 */
@Composable
fun rememberSonidos(): Sonidos {
    val context = LocalContext.current
    val sonidos = remember { Sonidos(context.applicationContext) }
    DisposableEffect(sonidos) {
        onDispose { sonidos.liberar() }
    }
    return sonidos
}
