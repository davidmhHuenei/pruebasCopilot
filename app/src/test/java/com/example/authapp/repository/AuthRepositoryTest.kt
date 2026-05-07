package com.example.authapp.repository

import com.example.authapp.model.LoginRequest
import com.example.authapp.model.RegisterRequest
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test

class AuthRepositoryTest {

    @Test
    fun login_withValidCredentials_returnsSuccess() = runTest {
        val response = AuthRepository.login(LoginRequest("testuser", "password123"))
        assertTrue(response.success)
        assertNotNull(response.user)
        assertEquals("testuser", response.user?.username)
        assertEquals("12345678", response.user?.documento)
    }

    @Test
    fun login_withInvalidPassword_returnsError() = runTest {
        val response = AuthRepository.login(LoginRequest("testuser", "wrongpassword"))
        assertFalse(response.success)
        assertNull(response.user)
    }

    @Test
    fun login_withNonexistentUser_returnsError() = runTest {
        val response = AuthRepository.login(LoginRequest("unknown", "password"))
        assertFalse(response.success)
        assertNull(response.user)
    }

    @Test
    fun register_withNewUser_returnsSuccess() = runTest {
        val response = AuthRepository.register(RegisterRequest("87654321", "newuser", "pass123"))
        assertTrue(response.success)
    }

    @Test
    fun register_withDuplicateUsername_returnsError() = runTest {
        val response = AuthRepository.register(RegisterRequest("99999999", "testuser", "pass123"))
        assertFalse(response.success)
    }

    @Test
    fun register_withDuplicateDocument_returnsError() = runTest {
        val response = AuthRepository.register(RegisterRequest("12345678", "anotheruser", "pass123"))
        assertFalse(response.success)
    }

    @Test
    fun login_afterRegisteringNewUser_returnsSuccess() = runTest {
        AuthRepository.register(RegisterRequest("11111111", "freshuser", "mypassword"))
        val response = AuthRepository.login(LoginRequest("freshuser", "mypassword"))
        assertTrue(response.success)
    }
}
