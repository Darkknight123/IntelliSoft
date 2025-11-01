package com.app.intellisoft.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.intellisoft.data.remote.models.LoginRequest
import com.app.intellisoft.data.remote.models.RegisterRequest
import com.app.intellisoft.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState = _authState.asStateFlow()

    fun register(email: String, firstname: String, lastname: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val response = repository.registerUser(
                    RegisterRequest(
                        email,
                        firstname,
                        lastname,
                        password
                    )
                )
                _authState.value = AuthState.Success(response.message ?: "Registration Successful")
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Registration failed")
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val response = repository.loginUser(LoginRequest(email, password))
                _authState.value = AuthState.Success(response.message ?: "Login Successful")
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Login failed")
            }
        }
    }
}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val message: String) : AuthState()
    data class Error(val error: String) : AuthState()
}
