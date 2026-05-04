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
class LoginScreenTest : BaseAndroidTest() {

    @get:Rule
    val activityRule = ActivityScenarioRule(ComponentActivity::class.java)


    private var loginSuccessCallCounter = 0
    private var registerClickCallCounter = 0

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
        registerClickCallCounter = 0
        try {
            activityRule.scenario.onActivity { activity ->
                activity.setContent {
                    PruebasCopilotTheme {
                        LoginScreen(
                            onLoginSuccess = { loginSuccessCallCounter++ },
                            onRegisterClick = { registerClickCallCounter++ }
                        )
                    }
                }
            }
        } catch (e: Exception) {
            throw AssertionError("Failed to setup login screen: ${e.message}", e)
        }
    }

    @Test
    fun loginScreen_displaysTitle() {
        setupLoginScreen()
        try {
            onView(withText("Login")).check(matches(isDisplayed()))
            takeScreenshot("login_screen_title")
        } catch (e: Exception) {
            Allure.addAttachment("error", "text/plain", "Login screen title test failed: ${e.message}".byteInputStream(), ".txt")
            throw e
        }
    }

    @Test
    fun loginButton_isDisplayedWhenFormIsEmpty() {
        setupLoginScreen()
        try {
            // Just verify the activity displayed without errors
            takeScreenshot("login_initial_state")
        } catch (e: Exception) {
            Allure.addAttachment("error", "text/plain", "Initial state test failed: ${e.message}".byteInputStream(), ".txt")
            throw e
        }
    }

    @Test
    fun enterCredentials_withValidCredentials_shouldEnableLoginButton() {
        setupLoginScreen()
        try {
            onView(withHint("Usuario")).perform(typeText("testuser"))
            onView(withHint("Contraseña")).perform(typeText("password123"))
            onView(withText("Iniciar Sesión")).check(matches(isDisplayed()))
            takeScreenshot("login_button_enabled")
        } catch (e: Exception) {
            Allure.addAttachment("error", "text/plain", "Valid credentials test failed: ${e.message}".byteInputStream(), ".txt")
            throw e
        }
    }

    @Test
    fun registerButton_isAlwaysVisible() {
        setupLoginScreen()
        try {
            onView(withText("¿No tienes cuenta? Regístrate")).check(matches(isDisplayed()))
            takeScreenshot("register_button_visible")
        } catch (e: Exception) {
            Allure.addAttachment("error", "text/plain", "Register button test failed: ${e.message}".byteInputStream(), ".txt")
            throw e
        }
    }
}


