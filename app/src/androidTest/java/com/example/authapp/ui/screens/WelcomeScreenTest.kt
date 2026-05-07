package com.example.authapp.ui.screens

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.authapp.MainActivity
import org.junit.Rule
import org.junit.Test

class WelcomeScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun welcome_sinUsuario_muestraPantallaInicial() {
        composeTestRule.onNodeWithText("Bienvenido")
    }
}
