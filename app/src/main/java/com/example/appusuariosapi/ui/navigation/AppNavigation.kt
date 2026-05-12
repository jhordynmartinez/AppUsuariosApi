package com.example.appusuariosapi.ui.navigation

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.*
import com.example.appusuariosapi.data.SessionManager
import com.example.appusuariosapi.ui.screens.*
import com.example.appusuariosapi.viewmodel.AuthViewModel

@Composable
fun AppNavigation(context: Context) {
    // Inicializamos el gestor de sesión y el ViewModel de autenticación
    val sessionManager = remember { SessionManager(context) }
    val viewModel = remember { AuthViewModel(sessionManager) }

    // Observamos el estado de 'isLoggedIn' desde DataStore. 
    // Al ser un Flow, usamos collectAsState. El valor inicial es null mientras carga.
    val isLoggedIn by sessionManager.isLoggedIn.collectAsState(initial = null)
    val navController = rememberNavController()

    // Lógica de decisión de pantalla inicial:
    // Si es null, DataStore aún está leyendo el archivo de preferencias.
    if (isLoggedIn == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        // Una vez que tenemos un valor (true/false), definimos el NavHost.
        // startDestination se elige dinámicamente según el flag persistente.
        NavHost(
            navController = navController,
            startDestination = if (isLoggedIn == true) "home" else "login"
        ) {
            // Pantalla de autenticación (Login y Registro)
            composable("login") { 
                LoginScreen(viewModel, navController) 
            }
            // Pantalla principal protegida por sesión
            composable("home") { 
                HomeScreen(viewModel, navController) 
            }
        }
    }
}
