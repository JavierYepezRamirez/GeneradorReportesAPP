package com.cinergia.cinercia

import com.google.gson.annotations.SerializedName

data class Nodo(
    val id: String,

    @SerializedName("descripcion")
    val descripcion: Descripcion
)

data class Descripcion(
    val nombre: String,
    val municipio: String,
    val comunidad: String? = null,
    val direccion: String? = null,
    val tipo: String? = null,
    val latitud: String? = null,
    val longitud: String? = null,
    val telefono: String? = null,
    val correo: String? = null,
    val claveNodo: String? = null,
    val codigoPostal: String? = null,
    val nomenclatura: String? = null
)
