package com.example.appusuariosapi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.*
//hola
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppNavegacion()
        }
    }
}

@Composable
fun AppNavegacion() {
    val navController = rememberNavController() // Controlador de rutas

    // Scaffold es la estructura básica (barra superior, etc)
    Scaffold { padding ->
        NavHost(navController, startDestination = "menu", modifier = Modifier.padding(padding)) {
            composable("menu") { PantallaMenu(navController) }
            composable("home") { PantallaHome(navController) }
            composable("form") { PantallaForm(navController) }
            // Recibimos un argumento "nombre" en la ruta
            composable("saludo/{nombre}") { backStackEntry ->
                val nombre = backStackEntry.arguments?.getString("nombre") ?: ""
                PantallaSaludo(nombre, navController)
            }
        }
    }
}

@Composable
fun PantallaMenu(navController: androidx.navigation.NavController) {
    Column(Modifier.fillMaxSize(), Arrangement.Center, Alignment.CenterHorizontally) {
        Button(onClick = { navController.navigate("home") }) { Text("Ir a Home") }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.navigate("form") }) { Text("Ir a Form") }
    }
}

@Composable
fun PantallaHome(navController: androidx.navigation.NavController) {
    Column(Modifier.fillMaxSize(), Arrangement.Center, Alignment.CenterHorizontally) {
        Text("Hola Mundo")
        Button(onClick = { navController.popBackStack() }) { Text("Retroceder") }
    }
}

@Composable
fun PantallaForm(navController: androidx.navigation.NavController) {
    var texto by remember { mutableStateOf("") }
    Column(Modifier.fillMaxSize(), Arrangement.Center, Alignment.CenterHorizontally) {
        TextField(value = texto, onValueChange = { texto = it }, label = { Text("Escribe algo") })
        Button(onClick = { navController.navigate("saludo/$texto") }) { Text("Enviar") }
    }
}

@Composable
fun PantallaSaludo(nombre: String, navController: androidx.navigation.NavController) {
    Column(Modifier.fillMaxSize(), Arrangement.Center, Alignment.CenterHorizontally) {
        Text("Hola: $nombre")
        Button(onClick = { navController.popBackStack() }) { Text("Volver") }
    }
}