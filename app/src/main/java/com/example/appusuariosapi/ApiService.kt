package com.example.appusuariosapi
import retrofit2.http.GET

interface ApiService {
    @GET("usuarios")
    suspend fun getUsuarios(): List<Usuario>
}