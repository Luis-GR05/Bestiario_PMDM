package com.pm2627.p1ev.memory.ui.componentes

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pm2627.p1ev.memory.R
import com.pm2627.p1ev.memory.model.Carta
import com.pm2627.p1ev.memory.model.Criatura
import com.pm2627.p1ev.memory.model.EstadoCarta
import com.pm2627.p1ev.memory.ui.theme.MemoryTheme

/**
 * UNA CARTA, sin estado propio (Stateless): recibe la carta y avisa del toque.
 *
 * Solo pinta lo que dice carta.estado: el reverso si está boca abajo y el grabado de la
 * criatura si está boca arriba o emparejada. No decide nada: el toque lo resuelve el ViewModel.
 * Las imágenes se muestran con Image y painterResource (apuntes UT2 2.3.2 §3).
 */
@Composable
fun CartaView(
    carta: Carta,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val descripcion = if (carta.bocaAbajo) "Carta boca abajo" else carta.criatura.nombre

    Card(
        onClick = onClick,
        modifier = modifier
            .aspectRatio(3f / 4f)
            .semantics { contentDescription = descripcion },
        shape = RoundedCornerShape(10.dp),
        border = if (carta.emparejada) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null,
    ) {
        if (carta.bocaAbajo) {
            Reverso()
        } else {
            Cara(criatura = carta.criatura, emparejada = carta.emparejada)
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

/** La cara: el grabado de la criatura y su nombre. */
@Composable
private fun Cara(criatura: Criatura, emparejada: Boolean) {
    Box(Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize().padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Image(
                painter = painterResource(criatura.imagen),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(4.dp)),
            )
            Text(
                text = criatura.nombre,
                style = MaterialTheme.typography.labelMedium,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        if (emparejada) {
            // Pareja resuelta: velo oscuro encima (y el borde lo pone la Card)
            Box(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.35f)))
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