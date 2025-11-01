package com.app.intellisoft.presentation.screens.onboarding

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.app.intellisoft.data.local.TokenManager
import com.app.intellisoft.data.repository.AuthRepository
import com.app.intellisoft.presentation.viewmodel.AuthState
import com.app.intellisoft.presentation.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit
) {
    val context = LocalContext.current
    val tokenManager = remember { TokenManager.getInstance(context) }
    val repository = remember { AuthRepository(tokenManager) }
    val viewModel = remember { AuthViewModel(repository) }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val state by viewModel.authState.collectAsState()

    LaunchedEffect(state) {
        if (state is AuthState.Success) {
            val savedToken = tokenManager.getToken()
            Log.d("TokenManager", "Token after login: $savedToken")
            onLoginSuccess()
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Login") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Password") }, modifier = Modifier.fillMaxWidth())

            Spacer(Modifier.height(16.dp))
            Button(onClick = { viewModel.login(email, password) }, modifier = Modifier.fillMaxWidth()) {
                Text("Login")
            }

            when (state) {
                is AuthState.Loading -> Text("Logging in...", color = Color.Gray)
                is AuthState.Success -> {
                    Text((state as AuthState.Success).message, color = Color.Green)
                    onLoginSuccess()
                }
                is AuthState.Error -> Text((state as AuthState.Error).error, color = Color.Red)
                else -> {}
            }
        }
    }
}
