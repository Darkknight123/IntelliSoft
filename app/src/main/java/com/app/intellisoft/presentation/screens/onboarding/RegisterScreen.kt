package com.app.intellisoft.presentation.screens.onboarding

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
fun RegisterScreen(
    onSignupSuccess: () -> Unit
) {
    val context = LocalContext.current
    val tokenManager = remember { TokenManager.getInstance(context) }
    val repository = remember { AuthRepository(tokenManager) }
    val viewModel = remember { AuthViewModel(repository) }

    var email by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val state by viewModel.authState.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Register") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = firstName, onValueChange = { firstName = it }, label = { Text("First Name") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = lastName, onValueChange = { lastName = it }, label = { Text("Last Name") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Password") }, modifier = Modifier.fillMaxWidth())

            Spacer(Modifier.height(16.dp))
            Button(onClick = { viewModel.register(email, firstName, lastName, password) }, modifier = Modifier.fillMaxWidth()) {
                Text("Register")
            }

            Spacer(Modifier.height(16.dp))

            Button(onClick = {
                onSignupSuccess()
            }, modifier = Modifier.fillMaxWidth()) {
                Text("Login")
            }

            when (state) {
                is AuthState.Loading -> Text("Registering...", color = Color.Gray)
                is AuthState.Success -> {
                    Text((state as AuthState.Success).message, color = Color.Green)
                    onSignupSuccess()
                }
                is AuthState.Error -> Text((state as AuthState.Error).error, color = Color.Red)
                else -> {}
            }
        }
    }
}
