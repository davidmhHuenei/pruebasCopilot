package com.example.pruebascopilot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.example.pruebascopilot.models.User
import com.example.pruebascopilot.ui.screens.LoginScreen
import com.example.pruebascopilot.ui.screens.RegisterScreen
import com.example.pruebascopilot.ui.screens.WelcomeScreen
import com.example.pruebascopilot.ui.theme.PruebasCopilotTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PruebasCopilotTheme {
                PruebasCopilotApp()
            }
        }
    }
}

@Composable
fun PruebasCopilotApp() {
    var currentScreen by rememberSaveable { 
        mutableStateOf<AuthScreen>(AuthScreen.Login) 
    }
    var loggedInUser by rememberSaveable { mutableStateOf<User?>(null) }

    when (currentScreen) {
        AuthScreen.Login -> {
            LoginScreen(
                onLoginSuccess = { user ->
                    loggedInUser = user
                    currentScreen = AuthScreen.Welcome
                },
                onRegisterClick = {
                    currentScreen = AuthScreen.Register
                }
            )
        }
        AuthScreen.Register -> {
            RegisterScreen(
                onRegisterSuccess = {
                    currentScreen = AuthScreen.Login
                },
                onBackClick = {
                    currentScreen = AuthScreen.Login
                }
            )
        }
        AuthScreen.Welcome -> {
            WelcomeScreen(
                user = loggedInUser,
                onLogout = {
                    loggedInUser = null
                    currentScreen = AuthScreen.Login
                }
            )
        }
    }
}

enum class AuthScreen {
    Login,
    Register,
    Welcome
}

@Preview(showBackground = true)
@Composable
fun PruebasCopilotAppPreview() {
    PruebasCopilotTheme {
        PruebasCopilotApp()
    }
}