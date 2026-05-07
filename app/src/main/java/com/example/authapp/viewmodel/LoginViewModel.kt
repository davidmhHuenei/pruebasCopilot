package com.example.authapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.authapp.model.LoginRequest
import com.example.authapp.model.User
import com.example.authapp.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class LoginUiState {
    data object Empty : LoginUiState()
    data object Loading : LoginUiState()
    data class Error(val message: String) : LoginUiState()
    data class Success(val user: User) : LoginUiState()
}

class LoginViewModel : ViewModel() {
    private val repository = AuthRepository

    private val _state = MutableStateFlow<LoginUiState>(LoginUiState.Empty)
    val state: StateFlow<LoginUiState> = _state.asStateFlow()

    fun login(username: String, password: String) {
        if (username.isBlank() || password.isBlank()) {
            _state.value = LoginUiState.Error("Todos los campos son obligatorios")
            return
        }
        _state.value = LoginUiState.Loading
        viewModelScope.launch {
            val response = repository.login(LoginRequest(username.trim(), password))
            _state.value = if (response.success && response.user != null) {
                LoginUiState.Success(response.user)
            } else {
                LoginUiState.Error(response.message)
            }
        }
    }

    fun resetState() {
        _state.value = LoginUiState.Empty
    }
}
