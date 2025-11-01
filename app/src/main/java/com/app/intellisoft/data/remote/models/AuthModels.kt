package com.app.intellisoft.data.remote.models

data class RegisterRequest(
    val email: String,
    val firstname: String,
    val lastname: String,
    val password: String
)

data class RegisterResponse(
    val message: String?,
    val token: String?,
    val success: Boolean?
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val message: String?,
    val `data`: Data,
    val token: String?,
    val success: Boolean?
)


data class Data(
    val access_token: String,
    val created_at: String,
    val email: String,
    val id: Int,
    val name: String,
    val updated_at: String
)