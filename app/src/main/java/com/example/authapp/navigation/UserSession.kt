package com.example.authapp.navigation

import com.example.authapp.model.User

object UserSession {
    var currentUser: User? = null
    var registeredUsername: String = ""
}
