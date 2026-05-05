package com.example.appusuariosapi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.*
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // SetContent define que vamos a usar Jetpack Compose para dibujar la interfaz
        setContent {
            AppNavegacion()
        }
    }
}

@Composable
fun AppNavegacion() {
    // rememberNavController: Objeto que gestiona la navegación entre pantallas (el historial de pantallas)
    val navController = rememberNavController()

    // Scaffold: Provee estructura básica de Material Design (permite padding, barras, etc.)
    Scaffold { padding ->
        // NavHost: Es el contenedor que maneja qué pantalla mostrar según la ruta ("menu", "home", etc.)
        NavHost(navController, startDestination = "menu", modifier = Modifier.padding(padding)) {
            composable("menu") { PantallaMenu(navController) }
            composable("home") { PantallaHome(navController) }
            composable("form") { PantallaForm(navController) }
            // Ruta con parámetros: {nombre} es el valor que pasaremos de una pantalla a otra
            composable("saludo/{nombre}") { backStackEntry ->
                val nombre = backStackEntry.arguments?.getString("nombre") ?: ""
                PantallaSaludo(nombre, navController)
            }
            composable("firebase") { PantallaFirebase(navController) }
        }
    }
}

//SEMANA 7
@Composable
fun PantallaFirebase(navController: NavController) {
    // Obtenemos la instancia de Firestore (la base de datos en la nube de Google)
    val db = FirebaseFirestore.getInstance()

    // Estados para los campos de texto del formulario
    var nombre by remember { mutableStateOf("") }
    var edad by remember { mutableStateOf("") }
    // Estado para guardar la lista de datos que vienen de la nube
    var listaPersonas by remember { mutableStateOf(listOf<Map<String, Any>>()) }

    // escribir datos en Firestore
    fun guardarDatos() {
        // Creamos un mapa (clave-valor) que es el formato que Firestore entiende
        val persona = hashMapOf("nombre" to nombre, "edad" to edad)
        // Guardamos
        db.collection("usuarios").add(persona)
        nombre = ""; edad = "" // Limpiar
    }

    //Se ejecuta una sola vez al entrar a la pantalla
    LaunchedEffect(Unit) {
        // addSnapshotListener: Se queda "escuchando" cambios en la colección "usuarios".
        // Cada vez que alguien agregue, borre o modifique un dato, se ejecutará el código interno.
        db.collection("usuarios").addSnapshotListener { snapshot, _ ->
            if (snapshot != null) {
                // Convertimos los documentos de Firebase a una lista manejable por nuestra UI
                listaPersonas = snapshot.documents.map { it.data ?: emptyMap() }
            }
        }
    }

    Column(Modifier.padding(16.dp)) {
        Button(onClick = { navController.popBackStack() }) { Text("Volver al menú") }
        TextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") })
        TextField(value = edad, onValueChange = { edad = it }, label = { Text("Edad") })
        Button(onClick = { guardarDatos() }) { Text("Guardar en Firebase") }

        Spacer(modifier = Modifier.height(20.dp))

        // LazyColumn: Lista eficiente que solo renderiza los elementos visibles en pantalla
        LazyColumn {
            items(listaPersonas) { persona ->
                Card(modifier = Modifier.padding(vertical = 4.dp).fillMaxWidth()) {
                    Text(
                        text = "Nombre: ${persona["nombre"]}, Edad: ${persona["edad"]}",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun PantallaMenu(navController: NavController) {
    Column(Modifier.fillMaxSize(), Arrangement.Center, Alignment.CenterHorizontally) {
        Button(onClick = { navController.navigate("home") }) { Text("Ir a Home") }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.navigate("form") }) { Text("Ir a Form") }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.navigate("firebase") }) { Text("Ir a Firebase") }
    }
}

@Composable
fun PantallaHome(navController: NavController) {
    Column(Modifier.fillMaxSize(), Arrangement.Center, Alignment.CenterHorizontally) {
        Text("Hola Mundo")
        Button(onClick = { navController.popBackStack() }) { Text("Retroceder") }
    }
}

@Composable
fun PantallaForm(navController: NavController) {
    var texto by remember { mutableStateOf("") }
    Column(Modifier.fillMaxSize(), Arrangement.Center, Alignment.CenterHorizontally) {
        TextField(value = texto, onValueChange = { texto = it }, label = { Text("Escribe algo") })
        Button(onClick = { navController.navigate("saludo/$texto") }) { Text("Enviar") }
    }
}

@Composable
fun PantallaSaludo(nombre: String, navController: NavController) {
    Column(Modifier.fillMaxSize(), Arrangement.Center, Alignment.CenterHorizontally) {
        Text("Hola: $nombre")
        Button(onClick = { navController.popBackStack() }) { Text("Volver") }
    }
}