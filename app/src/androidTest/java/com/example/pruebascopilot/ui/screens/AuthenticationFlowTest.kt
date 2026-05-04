package com.example.pruebascopilot.ui.screens

import androidx.activity.ComponentActivity
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.pruebascopilot.BaseAndroidTest
import com.example.pruebascopilot.ui.theme.PruebasCopilotTheme
import io.qameta.allure.junit4.AllureJunit4
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.onIdle
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withHint
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.core.IsNot.not
import androidx.activity.compose.setContent
import androidx.test.uiautomator.UiDevice
import androidx.test.platform.app.InstrumentationRegistry
import io.qameta.allure.Allure
import java.io.File
import java.io.FileInputStream
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AuthenticationFlowTest : BaseAndroidTest() {

    @get:Rule
    val activityRule = ActivityScenarioRule(ComponentActivity::class.java)


    private var loginSuccessCallCounter = 0
    private var registerSuccessCallCounter = 0
    private var backClickCallCounter = 0

    private fun takeScreenshot(name: String) {
        try {
            ensurePermissions()
            createDirectoriesIfNeeded()

            val file = saveScreenshot(name)
            if (file != null && file.exists()) {
                Allure.addAttachment(name, "image/png", FileInputStream(file), ".png")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setupLoginScreen() {
        loginSuccessCallCounter = 0
        backClickCallCounter = 0
        try {
            activityRule.scenario.onActivity { activity ->
                activity.setContent {
                    PruebasCopilotTheme {
                        LoginScreen(
                            onLoginSuccess = { loginSuccessCallCounter++ },
                            onRegisterClick = { backClickCallCounter++ }
                        )
                    }
                }
            }
        } catch (e: Exception) {
            throw AssertionError("Failed to setup login screen: ${e.message}", e)
        }
    }

    private fun setupRegisterScreen() {
        registerSuccessCallCounter = 0
        backClickCallCounter = 0
        try {
            activityRule.scenario.onActivity { activity ->
                activity.setContent {
                    PruebasCopilotTheme {
                        RegisterScreen(
                            onRegisterSuccess = { registerSuccessCallCounter++ },
                            onBackClick = { backClickCallCounter++ }
                        )
                    }
                }
            }
        } catch (e: Exception) {
            throw AssertionError("Failed to setup register screen: ${e.message}", e)
        }
    }

    @Test
    fun loginFlow_withValidCredentials_shouldProceed() {
        setupLoginScreen()
        try {
            onView(withHint("Usuario")).perform(typeText("testuser"))
            onView(withHint("Contraseña")).perform(typeText("password123"))
            onView(withText("Iniciar Sesión")).check(matches(isDisplayed()))
            takeScreenshot("login_flow_start")
        } catch (e: Exception) {
            Allure.addAttachment("error", "text/plain", "Login flow test failed: ${e.message}".byteInputStream(), ".txt")
            throw e
        }
    }

    @Test
    fun registerFlow_navigateToRegistration_shouldDisplayRegisterScreen() {
        setupLoginScreen()
        try {
            onView(withText("¿No tienes cuenta? Regístrate")).check(matches(isDisplayed()))
            takeScreenshot("register_navigation_button")
        } catch (e: Exception) {
            Allure.addAttachment("error", "text/plain", "Register navigation test failed: ${e.message}".byteInputStream(), ".txt")
            throw e
        }
    }

    @Test
    fun registerFlow_fillFormAndSubmit_withValidData_shouldDisplay() {
        setupRegisterScreen()
        try {
            onView(withHint("Documento")).perform(typeText("55555555"))
            onView(withHint("Usuario")).perform(typeText("newuser"))
            onView(withHint("Contraseña")).perform(typeText("password123"))
            onView(withText("Registrarse")).check(matches(isDisplayed()))
            takeScreenshot("register_form_filled")
        } catch (e: Exception) {
            Allure.addAttachment("error", "text/plain", "Register form test failed: ${e.message}".byteInputStream(), ".txt")
            throw e
        }
    }

    @Test
    fun passwordField_shouldAlwaysBeMasked() {
        setupLoginScreen()
        try {
            onView(withHint("Contraseña")).perform(typeText("secretpassword123"))
            onView(withHint("Contraseña")).check(matches(isDisplayed()))
            takeScreenshot("password_field_masking")
        } catch (e: Exception) {
            Allure.addAttachment("error", "text/plain", "Password masking test failed: ${e.message}".byteInputStream(), ".txt")
            throw e
        }
    }
}
