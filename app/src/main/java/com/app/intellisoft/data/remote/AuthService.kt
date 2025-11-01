package com.app.intellisoft.data.remote

import com.app.intellisoft.data.remote.models.LoginRequest
import com.app.intellisoft.data.remote.models.LoginResponse
import com.app.intellisoft.data.remote.models.RegisterRequest
import com.app.intellisoft.data.remote.models.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthService {

    @Headers("Accept: application/json")
    @POST("user/signup")
    suspend fun registerUser(@Body request: RegisterRequest): RegisterResponse

    @Headers("Accept: application/json")
    @POST("user/signin")
    suspend fun loginUser(@Body request: LoginRequest): LoginResponse
}