package com.pm2627.p1ev.memory.ui.componentes

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pm2627.p1ev.memory.R
import com.pm2627.p1ev.memory.model.Carta
import com.pm2627.p1ev.memory.model.Criatura
import com.pm2627.p1ev.memory.model.EstadoCarta
import com.pm2627.p1ev.memory.model.TipoCriatura
import com.pm2627.p1ev.memory.ui.theme.Cuero
import com.pm2627.p1ev.memory.ui.theme.MemoryTheme
import com.pm2627.p1ev.memory.ui.theme.Musgo
import com.pm2627.p1ev.memory.ui.theme.Oro
import com.pm2627.p1ev.memory.ui.theme.Pergamino
import com.pm2627.p1ev.memory.ui.theme.PergaminoOscuro
import com.pm2627.p1ev.memory.ui.theme.Sangre
import com.pm2627.p1ev.memory.ui.theme.Tinta

const val DURACION_GIRO_MS = 450

/**
 * UNA CARTA, sin estado propio (Stateless): recibe la carta y avisa del toque.
 *
 * Investigación 6.1 · volteo animado (apuntes 3A.4 §1 y §2):
 *  - Una animación no guarda nada. El estado decide el destino (boca arriba o abajo) y la
 *    animación calcula cómo llegar: animateFloatAsState recorre los valores intermedios de 0º a 180º.
 *  - graphicsLayer lee el ángulo al dibujar y gira la carta en el eje Y (rotationY).
 *    cameraDistance aleja la "cámara" para que la perspectiva no deforme la carta al girar.
 *  - Hasta 90º se ve el reverso; a partir de 90º, la cara. Como la cara está girada
 *    más de 90º se vería en espejo, así que le aplico otros 180º y queda derecha.
 */
@Composable
fun CartaView(
    carta: Carta,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val angulo by animateFloatAsState(
        targetValue = if (carta.bocaAbajo) 0f else 180f,
        animationSpec = tween(durationMillis = DURACION_GIRO_MS),
        label = "giroCarta",
    )
    val descripcion = if (carta.bocaAbajo) "Carta boca abajo" else carta.criatura.nombre

    Card(
        onClick = onClick,
        modifier = modifier
            .aspectRatio(3f / 4f)
            .semantics { contentDescription = descripcion }
            .graphicsLayer {
                rotationY = angulo
                cameraDistance = 12f * density
            },
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Cuero),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        border = if (carta.emparejada) BorderStroke(2.dp, Oro) else null,
    ) {
        if (angulo <= 90f) {
            Reverso()
        } else {
            Cara(
                criatura = carta.criatura,
                emparejada = carta.emparejada,
                modifier = Modifier.graphicsLayer { rotationY = 180f },
            )
        }
    }
}

@Composable
private fun Reverso() {
    Image(
        painter = painterResource(R.drawable.carta_reverso),
        contentDescription = null,
        contentScale = ContentScale.FillBounds,
        modifier = Modifier.fillMaxSize(),
    )
}

/** La cara: el grabado de la criatura sobre pergamino, con su insignia y su nombre. */
@Composable
private fun Cara(criatura: Criatura, emparejada: Boolean, modifier: Modifier = Modifier) {
    val colorTipo = if (criatura.tipo == TipoCriatura.MITICA) Sangre else Musgo
    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Pergamino)
                .padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Text(
                text = criatura.tipo.etiqueta.uppercase(),
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 7.sp),
                color = Pergamino,
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(colorTipo)
                    .padding(horizontal = 5.dp, vertical = 1.dp),
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(4.dp))
                    .background(PergaminoOscuro),
            ) {
                Image(
                    painter = painterResource(criatura.imagen),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                )
            }
            Text(
                text = criatura.nombre,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                color = Tinta,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        if (emparejada) {
            // Pareja resuelta: velo oscuro encima (y el borde dorado lo pone la Card)
            Box(Modifier.fillMaxSize().background(Tinta.copy(alpha = 0.35f)))
        }
    }
}

@Preview
@Composable
private fun CartaViewPreview() {
    MemoryTheme {
        CartaView(Carta(id = 0, criatura = Criatura.HIDRA, estado = EstadoCarta.BOCA_ARRIBA), onClick = {})
    }
}
