package com.example.authapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.authapp.ui.theme.SuccessBlue
import com.example.authapp.viewmodel.RegisterUiState
import com.example.authapp.viewmodel.RegisterViewModel
import androidx.compose.foundation.text.KeyboardOptions

@Composable
fun RegisterScreen(
    onRegisterSuccess: (String) -> Unit,
    onBackClick: () -> Unit,
    viewModel: RegisterViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    var documento by remember { mutableStateOf("") }
    var usuario by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    LaunchedEffect(state) {
        if (state is RegisterUiState.Success) {
            onRegisterSuccess(usuario)
        }
    }

    val isLoading = state is RegisterUiState.Loading

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Registro", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = documento,
            onValueChange = {
                if (it.all { c -> c.isDigit() }) {
                    documento = it
                }
                viewModel.resetState()
            },
            label = { Text("Nro Documento") },
            modifier = Modifier
                .fillMaxWidth()
                .semantics { testTag = "documentoField" },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = usuario,
            onValueChange = {
                usuario = it
                viewModel.resetState()
            },
            label = { Text("Usuario") },
            modifier = Modifier
                .fillMaxWidth()
                .semantics { testTag = "usuarioField" },
            singleLine = true,
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                viewModel.resetState()
            },
            label = { Text("Contraseña") },
            modifier = Modifier
                .fillMaxWidth()
                .semantics { testTag = "passwordField" },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.height(16.dp))

        when (val s = state) {
            is RegisterUiState.Error -> {
                Text(
                    text = s.message,
                    color = Color.Red,
                    modifier = Modifier.padding(bottom = 8.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            RegisterUiState.Success -> {
                Text(
                    text = "Registro exitoso",
                    color = SuccessBlue,
                    modifier = Modifier.padding(bottom = 8.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            else -> {}
        }

        Button(
            onClick = {
                viewModel.register(documento, usuario, password)
            },
            modifier = Modifier.fillMaxWidth().height(48.dp),
            enabled = documento.isNotBlank() && usuario.isNotBlank() && password.isNotBlank() && !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp
                )
            } else {
                Text("Registrarse")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(
            onClick = onBackClick,
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            Text("Volver al Login")
        }
    }
}
