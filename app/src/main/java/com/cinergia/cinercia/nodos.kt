package com.cinergia.cinercia

import android.content.Context

data class NodoData(
    val nodo: String,
    val municipio: String,
    val direccion: String,
    val codigoPostal: String,
    val nombreSitio: String,
    val tipoEspacio: String,
    val latitud: String,
    val longitud: String,
    val claveNodo: String,
    val nomencaltura: String
)

fun leerNodosDesdeCSV(context: Context): List<NodoData> {
    val nodos = mutableListOf<NodoData>()
    val inputStream = context.assets.open("nodos.csv")
    inputStream.bufferedReader().useLines { lines ->
        lines.drop(1).forEach { line -> // salta encabezado
            val parts = line.split(",")
            if (parts.size >= 10) {
                nodos.add(
                    NodoData(
                        parts[3], parts[13], parts[17], parts[18], parts[3], parts[4], parts[15], parts[14], parts[16], parts[2]
                    )
                )
            }
        }
    }
    return nodos
}
