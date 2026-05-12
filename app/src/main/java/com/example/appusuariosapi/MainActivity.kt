package com.example.appusuariosapi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.appusuariosapi.ui.navigation.AppNavigation // Importa tu nueva navegación

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // AppNavigation ahora decide si mostrar Login o Home
            AppNavigation(this)
        }
    }
}