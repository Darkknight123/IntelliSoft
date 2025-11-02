package com.app.intellisoft.presentation.screens.onboarding

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
        topBar = {
            TopAppBar(
                title = { Text("🔐 Welcome Back", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFEEF3F9))
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFFF9FAFB), Color(0xFFE3F2FD))
                    )
                )
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "👋 Hello there!\nLet's get you signed in.",
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                color = Color(0xFF37474F)
            )

            Spacer(Modifier.height(24.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("📧 Email") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("🔒 Password") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = { viewModel.login(email, password) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("🚀 Login", fontSize = 18.sp)
            }

            Spacer(Modifier.height(16.dp))

            when (state) {
                is AuthState.Loading -> Text("⏳ Logging in...", color = Color.Gray)
                is AuthState.Success -> Text("✅ ${(state as AuthState.Success).message}", color = Color(0xFF2E7D32))
                is AuthState.Error -> Text("❌ ${(state as AuthState.Error).error}", color = Color.Red)
                else -> {}
            }
        }
    }
}
