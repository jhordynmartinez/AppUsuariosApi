package com.example.appusuariosapi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.appusuariosapi.ui.theme.AppUsuariosApiTheme
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppUsuariosApiTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    ListaUsuariosScreen()
                }
            }
        }
    }
}

@Composable
fun ListaUsuariosScreen() {
    // Estado para guardar los datos
    var usuarios by remember { mutableStateOf(listOf<Usuario>()) }

    // Lanzar la llamada a la red
    LaunchedEffect(Unit) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://69d7aba59c5ebb0918c826de.mockapi.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(ApiService::class.java)
        try {
            usuarios = api.getUsuarios()
        } catch (e: Exception) {
            e.printStackTrace() // si hay algún error
        }
    }

    // LazyColumn
    LazyColumn {
        items(usuarios) { usuario ->
            Card(modifier = Modifier.padding(8.dp).fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Nombre: ${usuario.name}")
                    Text(text = "Email: ${usuario.email}")
                }
            }
        }
    }
}