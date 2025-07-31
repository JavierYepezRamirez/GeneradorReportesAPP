package com.cinergia.cinercia


data class Nodo(
    val id: String,
    val description: DescipcionNodo
)

data class DescipcionNodo (
    val nombre: String,
    val nomenclatura: String,
    val tipo: String,
    val comunidad: String,
    val municipio: String,
    val longitud: String,
    val latitud: String,
    val claveNodo: String,
    val direccion: String,
    val codigoPostal: String,
    val telefono: String,
    val correo: String
)