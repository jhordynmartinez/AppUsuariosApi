package com.example.appusuariosapi.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.appusuariosapi.viewmodel.AuthViewModel

@Composable
fun HomeScreen(viewModel: AuthViewModel, navController: NavController) {
    // Obtenemos el correo del usuario actual desde la instancia de Firebase Auth
    val userEmail = viewModel.auth.currentUser?.email ?: "Usuario"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Mensaje de bienvenida solicitado: Hola {correo_del_usuario}
        Text(
            text = "Hola $userEmail",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Botón Cerrar Sesión: Limpia Firebase y DataStore, luego redirige al Login
        Button(
            onClick = {
                viewModel.logout()
                // Navegamos al login y limpiamos el historial para evitar volver atrás
                navController.navigate("login") {
                    popUpTo("home") { inclusive = true }
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            )
        ) {
            Text("Cerrar Sesión")
        }
    }
}
