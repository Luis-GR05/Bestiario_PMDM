package com.pm2627.p1ev.memory

import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Test instrumentado (emulador o dispositivo): la app arranca en la pantalla de inicio.
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun inicio_muestraTituloYNiveles() {
        composeTestRule.onNodeWithText("BESTIARIO").assertExists()
        composeTestRule.onNodeWithText("Aprendiz").assertExists()
    }
}
