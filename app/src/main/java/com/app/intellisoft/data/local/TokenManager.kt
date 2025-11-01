package com.app.intellisoft.data.local

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit

class TokenManager private constructor(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    companion object {
        @Volatile
        private var INSTANCE: TokenManager? = null

        fun getInstance(context: Context): TokenManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: TokenManager(context.applicationContext).also { INSTANCE = it }
            }
        }
    }

    fun saveToken(token: String) {
        Log.d("TAG", "saveToken: $token")
        prefs.edit { putString("auth_token", token) }
    }

    fun getToken(): String? {
        return prefs.getString("auth_token", null)
    }

    fun clearToken() {
        prefs.edit { remove("auth_token") }
    }
}
