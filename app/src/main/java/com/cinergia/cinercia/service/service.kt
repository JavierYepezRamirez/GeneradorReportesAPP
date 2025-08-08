package com.cinergia.cinercia.service

import android.app.DownloadManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.cinergia.cinercia.Nodo
import okhttp3.OkHttpClient
import okhttp3.Headers
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

object AuthService {
    private const val baseUrl = "https://redfish-polite-blatantly.ngrok-free.app"

    private var token: String? = null
    private var rol: String? = null
    private var nombre: String? = null

    private val client = OkHttpClient()
    private val gson = Gson()

    fun setToken(newToken: String?) {
        token = newToken
    }

    fun setRol(newRol: String?) {
        rol = newRol
    }

    fun setNombre(newNombre: String?) {
        nombre = newNombre
    }

    fun getToken(): String? = token
    fun getRol(): String? = rol
    fun getNombre(): String? = nombre

    fun login(usuario: String, password: String): Boolean {
        val url = "$baseUrl/login"

        val jsonBody = gson.toJson(mapOf("usuario" to usuario, "password" to password))
        val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
        val body = jsonBody.toRequestBody(mediaType)

        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                return false
            }
            val responseBody = response.body?.string() ?: return false
            val data = gson.fromJson(responseBody, JsonObject::class.java)

            token = data.get("token")?.asString
            rol = data.get("rol")?.asString
            nombre = data.get("nombre")?.asString

            return true
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    @Throws(IOException::class)
    fun getNodos(): List<Nodo> {
        val url = "$baseUrl/nodos"

        val headersBuilder = Headers.Builder()
            .add("Content-Type", "application/json")

        token?.let {
            headersBuilder.add("Authorization", "Bearer $it")
        }

        val request = okhttp3.Request.Builder()
            .url(url)
            .headers(headersBuilder.build())
            .get()
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                throw IOException("Error al obtener nodos: ${response.body?.string()}")
            }

            val body = response.body?.string()
                ?: throw IOException("Respuesta vacía del servidor")

            val listType = object : TypeToken<List<Nodo>>() {}.type
            Log.d("getNodos", "Respuesta JSON: $body")
            return gson.fromJson(body, listType)
        }
    }

    fun getActividades(): List<String> {
        val url = "https://generadorreportes-dae9e-default-rtdb.firebaseio.com/.json"

        val request = Request.Builder()
            .url(url)
            .get()
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                throw IOException("Error al obtener actividades: ${response.body?.string()}")
            }

            val responseBody = response.body?.string()
                ?: throw IOException("Respuesta vacía del servidor")

            Log.d("ResponseBody", responseBody)

            val listType = object : TypeToken<List<String>>() {}.type
            val actividadesList: List<String> = gson.fromJson(responseBody, listType)
                ?: throw IOException("No se pudo parsear actividades")

            return actividadesList
        }
    }
}
