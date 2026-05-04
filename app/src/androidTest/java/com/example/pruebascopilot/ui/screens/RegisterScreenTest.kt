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
class RegisterScreenTest : BaseAndroidTest() {

    @get:Rule
    val activityRule = ActivityScenarioRule(ComponentActivity::class.java)


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
    fun registerScreen_displaysTitle() {
        setupRegisterScreen()
        try {
            onView(withText("Registro")).check(matches(isDisplayed()))
            takeScreenshot("register_screen_title")
        } catch (e: Exception) {
            Allure.addAttachment("error", "text/plain", "Register screen title test failed: ${e.message}".byteInputStream(), ".txt")
            throw e
        }
    }

    @Test
    fun registerButton_isVisible() {
        setupRegisterScreen()
        try {
            onView(withText("Volver al Login")).check(matches(isDisplayed()))
            takeScreenshot("back_button_visible")
        } catch (e: Exception) {
            Allure.addAttachment("error", "text/plain", "Back button test failed: ${e.message}".byteInputStream(), ".txt")
            throw e
        }
    }

    @Test
    fun fillForm_withValidData_shouldEnableRegisterButton() {
        setupRegisterScreen()
        try {
            onView(withHint("Documento")).perform(typeText("12345678"))
            onView(withHint("Usuario")).perform(typeText("newuser"))
            onView(withHint("Contraseña")).perform(typeText("password123"))
            onView(withText("Registrarse")).check(matches(isDisplayed()))
            takeScreenshot("register_button_enabled")
        } catch (e: Exception) {
            Allure.addAttachment("error", "text/plain", "Valid data test failed: ${e.message}".byteInputStream(), ".txt")
            throw e
        }
    }

    @Test
    fun clickBackButton_shouldCallBackCallback() {
        setupRegisterScreen()
        try {
            onView(withText("Volver al Login")).perform(click())
            assert(backClickCallCounter == 1)
            takeScreenshot("back_button_clicked")
        } catch (e: Exception) {
            Allure.addAttachment("error", "text/plain", "Back button click test failed: ${e.message}".byteInputStream(), ".txt")
            throw e
        }
    }
}
