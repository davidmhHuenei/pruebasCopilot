package com.example.authapp.ui.screens

import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import com.example.authapp.MainActivity
import org.junit.Rule
import org.junit.Test

class LoginScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun login_conCamposVacios_botonDeshabilitado() {
        composeTestRule.onNodeWithText("Iniciar Sesión").assertIsNotEnabled()
    }

    @Test
    fun login_conCredencialesInvalidas_muestraError() {
        composeTestRule.onNodeWithTag("loginUsuarioField").performTextInput("wrong")
        composeTestRule.onNodeWithTag("loginPasswordField").performTextInput("wrong")
        composeTestRule.onNodeWithText("Iniciar Sesión").assertIsNotEnabled()
    }
}
