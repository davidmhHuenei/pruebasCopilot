package com.example.authapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.authapp.model.RegisterRequest
import com.example.authapp.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class RegisterUiState {
    data object Idle : RegisterUiState()
    data object Loading : RegisterUiState()
    data class Error(val message: String) : RegisterUiState()
    data object Success : RegisterUiState()
}

class RegisterViewModel : ViewModel() {
    private val repository = AuthRepository

    private val _state = MutableStateFlow<RegisterUiState>(RegisterUiState.Idle)
    val state: StateFlow<RegisterUiState> = _state.asStateFlow()

    fun register(documento: String, username: String, password: String) {
        if (documento.isBlank() || username.isBlank() || password.isBlank()) {
            _state.value = RegisterUiState.Error("Todos los campos son obligatorios")
            return
        }
        if (!documento.all { it.isDigit() }) {
            _state.value = RegisterUiState.Error("El documento solo debe contener números")
            return
        }
        _state.value = RegisterUiState.Loading
        viewModelScope.launch {
            val response = repository.register(RegisterRequest(documento.trim(), username.trim(), password))
            _state.value = if (response.success) {
                RegisterUiState.Success
            } else {
                RegisterUiState.Error(response.message)
            }
        }
    }

    fun resetState() {
        _state.value = RegisterUiState.Idle
    }
}
