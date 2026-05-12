package com.example.appusuariosapi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appusuariosapi.data.SessionManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class AuthViewModel(private val sessionManager: SessionManager) : ViewModel() {
    // Instancia de Firebase Auth para interactuar con el servicio
    val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // Función para iniciar sesión con correo y contraseña (sin validaciones previas)
    fun login(email: String, pass: String, onResult: (Boolean) -> Unit) {
        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Si el login es exitoso en Firebase, guardamos el flag en DataStore
                    viewModelScope.launch { sessionManager.saveSession(true) }
                    onResult(true)
                } else {
                    onResult(false)
                }
            }
    }

    // Función para registrar un nuevo usuario (sin validaciones previas)
    fun register(email: String, pass: String, onResult: (Boolean) -> Unit) {
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Al registrarse con éxito, también activamos la sesión persistente
                    viewModelScope.launch { sessionManager.saveSession(true) }
                    onResult(true)
                } else {
                    onResult(false)
                }
            }
    }

    // Función para cerrar sesión tanto en Firebase como en DataStore
    fun logout() {
        auth.signOut() // Cierra sesión en Firebase
        viewModelScope.launch { 
            sessionManager.saveSession(false) // Limpia el flag en DataStore
        }
    }
}
