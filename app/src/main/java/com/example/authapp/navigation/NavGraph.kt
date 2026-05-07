package com.example.authapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.authapp.ui.screens.LoginScreen
import com.example.authapp.ui.screens.RegisterScreen
import com.example.authapp.ui.screens.WelcomeScreen

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val WELCOME = "welcome"
}

@Composable
fun AuthNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Routes.LOGIN) {
        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = { user ->
                    UserSession.currentUser = user
                    navController.navigate(Routes.WELCOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onRegisterClick = {
                    navController.navigate(Routes.REGISTER)
                }
            )
        }
        composable(Routes.REGISTER) {
            RegisterScreen(
                onRegisterSuccess = { username ->
                    UserSession.registeredUsername = username
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        composable(Routes.WELCOME) {
            val user = UserSession.currentUser
            if (user != null) {
                WelcomeScreen(
                    user = user,
                    onLogout = {
                        UserSession.currentUser = null
                        navController.navigate(Routes.LOGIN) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}
