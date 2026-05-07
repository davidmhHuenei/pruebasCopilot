package com.example.authapp.model

data class LoginRequest(val username: String, val password: String)
data class LoginResponse(val success: Boolean, val message: String, val user: User? = null)
data class RegisterRequest(val documento: String, val username: String, val password: String)
data class RegisterResponse(val success: Boolean, val message: String)
data class User(val id: Int, val username: String, val documento: String)
