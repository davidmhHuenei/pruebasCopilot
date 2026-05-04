package com.example.pruebascopilot.repository

import com.example.pruebascopilot.models.LoginRequest
import com.example.pruebascopilot.models.LoginResponse
import com.example.pruebascopilot.models.RegisterRequest
import com.example.pruebascopilot.models.RegisterResponse
import com.example.pruebascopilot.models.User
import kotlinx.coroutines.delay

object AuthRepository {
    private val mockUsers = mutableListOf(
        User("1", "12345678", "testuser", "password123")
    )

    suspend fun login(request: LoginRequest): LoginResponse {
        delay(800)
        val user = mockUsers.find { it.usuario == request.usuario }

        return if (user != null && user.password == request.password) {
            LoginResponse(
                success = true,
                message = "Login exitoso",
                user = user,
                token = "mock_token_${user.id}"
            )
        } else {
            LoginResponse(
                success = false,
                message = "Usuario o contraseña incorrecto"
            )
        }
    }

    suspend fun register(request: RegisterRequest): RegisterResponse {
        delay(800)

        val userExists = mockUsers.any { it.usuario == request.usuario }
        if (userExists) {
            return RegisterResponse(
                success = false,
                message = "Usuario ya existe"
            )
        }

        val documentoExists = mockUsers.any { it.documento == request.documento }
        if (documentoExists) {
            return RegisterResponse(
                success = false,
                message = "Documento ya registrado"
            )
        }

        val newUser = User(
            id = (mockUsers.size + 1).toString(),
            documento = request.documento,
            usuario = request.usuario,
            password = request.password
        )
        mockUsers.add(newUser)

        return RegisterResponse(
            success = true,
            message = "Registro exitoso",
            userId = newUser.id
        )
    }
}

