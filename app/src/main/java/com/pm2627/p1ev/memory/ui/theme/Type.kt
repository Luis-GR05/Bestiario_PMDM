package com.pm2627.p1ev.memory.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.pm2627.p1ev.memory.R

// Cinzel (SIL Open Font License): letras de inscripción clásica para títulos y nombres.
// Es una fuente variable: con FontVariation elijo el grosor de cada estilo.
@OptIn(androidx.compose.ui.text.ExperimentalTextApi::class)
val Cinzel = FontFamily(
    Font(R.font.cinzel, FontWeight.Normal, variationSettings = FontVariation.Settings(FontVariation.weight(400))),
    Font(R.font.cinzel, FontWeight.SemiBold, variationSettings = FontVariation.Settings(FontVariation.weight(600))),
    Font(R.font.cinzel, FontWeight.Bold, variationSettings = FontVariation.Settings(FontVariation.weight(800))),
)

val Typography = Typography(
    displaySmall = TextStyle(fontFamily = Cinzel, fontWeight = FontWeight.Bold, fontSize = 40.sp, letterSpacing = 2.sp),
    headlineMedium = TextStyle(fontFamily = Cinzel, fontWeight = FontWeight.Bold, fontSize = 28.sp, letterSpacing = 1.sp),
    titleLarge = TextStyle(fontFamily = Cinzel, fontWeight = FontWeight.SemiBold, fontSize = 22.sp),
    titleMedium = TextStyle(fontFamily = Cinzel, fontWeight = FontWeight.SemiBold, fontSize = 17.sp),
    labelSmall = TextStyle(fontFamily = Cinzel, fontWeight = FontWeight.Bold, fontSize = 10.sp, letterSpacing = 1.sp),
    bodyLarge = TextStyle(fontFamily = FontFamily.Serif, fontSize = 16.sp, lineHeight = 22.sp),
    bodyMedium = TextStyle(fontFamily = FontFamily.Serif, fontSize = 14.sp, lineHeight = 20.sp),
)
