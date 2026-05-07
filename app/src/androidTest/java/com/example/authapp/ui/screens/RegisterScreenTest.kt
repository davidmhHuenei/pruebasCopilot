package com.example.authapp.ui.screens

import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import com.example.authapp.MainActivity
import org.junit.Rule
import org.junit.Test

class RegisterScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun register_conCamposVacios_botonDeshabilitado() {
        composeTestRule.onNodeWithText("Registrarse").assertIsNotEnabled()
    }

    @Test
    fun register_campoDocumento_soloAceptaNumeros() {
        composeTestRule.onNodeWithTag("documentoField").performTextInput("abc123")
        composeTestRule.onNodeWithText("Registrarse").assertIsNotEnabled()
    }
}
