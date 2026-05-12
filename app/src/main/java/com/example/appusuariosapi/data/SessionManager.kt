package com.example.appusuariosapi.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

// Extensión para simplificar el acceso a DataStore desde cualquier parte del contexto
val Context.dataStore by preferencesDataStore(name = "session_prefs")

class SessionManager(private val context: Context) {
    // Definimos la llave para el flag de sesión (booleano)
    private val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")

    // Función para guardar el estado de la sesión de forma persistente
    suspend fun saveSession(isLoggedIn: Boolean) {
        context.dataStore.edit { it[IS_LOGGED_IN] = isLoggedIn }
    }

    // Flujo que emite el estado actual de la sesión. 
    // Si no existe el valor, por defecto es false (no logueado).
    val isLoggedIn = context.dataStore.data.map { it[IS_LOGGED_IN] ?: false }
}
