package com.app.intellisoft.data.repository

import android.content.SharedPreferences
import android.util.Log
import com.app.intellisoft.data.local.TokenManager
import com.app.intellisoft.data.remote.ApiClient
import com.app.intellisoft.data.remote.AuthService

import com.app.intellisoft.data.remote.models.LoginRequest
import com.app.intellisoft.data.remote.models.LoginResponse
import com.app.intellisoft.data.remote.models.RegisterRequest
import com.app.intellisoft.data.remote.models.RegisterResponse

class AuthRepository(
     private val tokenManager: TokenManager
) {

    private val api = ApiClient.retrofit.create(AuthService::class.java)

    suspend fun registerUser(request: RegisterRequest): RegisterResponse {
        return api.registerUser(request)
    }

    suspend fun loginUser(request: LoginRequest): LoginResponse {
        val response = api.loginUser(request)
        response.data.access_token.let {
            Log.d("TAG", "loginUser: ")
            tokenManager.saveToken(it)
        }
        Log.d("TAG", "token saved: ")
        return response
    }

}

