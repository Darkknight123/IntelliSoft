package com.app.intellisoft

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.app.intellisoft.presentation.navigation.AppNavHost
import com.app.intellisoft.ui.theme.IntelliSoftTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            IntelliSoftTheme {
                val navController = rememberNavController()
                AppNavHost(navController = navController)
            }
        }
    }
}
