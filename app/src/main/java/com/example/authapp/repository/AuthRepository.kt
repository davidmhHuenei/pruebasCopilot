package com.example.authapp.repository

import com.example.authapp.model.*
import kotlinx.coroutines.delay

object AuthRepository {
    private val users = mutableListOf(
        User(id = 1, username = "testuser", documento = "12345678")
    )
    private val passwords = mutableMapOf(
        "testuser" to "password123"
    )
    private var nextId = 2

    suspend fun login(request: LoginRequest): LoginResponse {
        delay(800)
        val user = users.find { it.username == request.username }
        return if (user != null && passwords[request.username] == request.password) {
            LoginResponse(success = true, message = "Inicio de sesión exitoso", user = user)
        } else {
            LoginResponse(success = false, message = "Usuario o contraseña incorrectos")
        }
    }

    suspend fun register(request: RegisterRequest): RegisterResponse {
        delay(800)
        val usernameExists = users.any { it.username == request.username }
        if (usernameExists) {
            return RegisterResponse(success = false, message = "El usuario ya existe")
        }
        val documentoExists = users.any { it.documento == request.documento }
        if (documentoExists) {
            return RegisterResponse(success = false, message = "El documento ya existe")
        }
        val newUser = User(id = nextId++, username = request.username, documento = request.documento)
        users.add(newUser)
        passwords[request.username] = request.password
        return RegisterResponse(success = true, message = "Registro exitoso")
    }

    fun getPrecargadoUsername(): String = "testuser"
    fun getPrecargadoPassword(): String = "password123"
}
