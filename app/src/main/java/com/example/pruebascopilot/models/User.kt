package com.example.pruebascopilot.models

data class User(
    val id: String,
    val documento: String,
    val usuario: String,
    val password: String
)

data class LoginRequest(
    val usuario: String,
    val password: String
)

data class LoginResponse(
    val success: Boolean,
    val message: String,
    val user: User? = null,
    val token: String? = null
)

data class RegisterRequest(
    val documento: String,
    val usuario: String,
    val password: String
)

data class RegisterResponse(
    val success: Boolean,
    val message: String,
    val userId: String? = null
)
