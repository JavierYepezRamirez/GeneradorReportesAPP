package com.cinergia.cinercia.Generador

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.cinergia.cinercia.R
import java.io.*
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream

class ReporteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reporte2)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Recuperar datos
        val nodo = intent.getStringExtra("nodo") ?: ""
        val municipio = intent.getStringExtra("municipio") ?: ""
        val direccion = intent.getStringExtra("direccion") ?: ""
        val codigoPostal = intent.getStringExtra("codigoPostal") ?: ""
        val nombreSitio = intent.getStringExtra("nombreSitio") ?: ""
        val tipoEspacio = intent.getStringExtra("tipoEspacio") ?: ""
        val latitud = intent.getStringExtra("latitud") ?: ""
        val longitud = intent.getStringExtra("longitud") ?: ""
        val claveNodo = intent.getStringExtra("claveNodo") ?: ""
        val nomencaltura = intent.getStringExtra("nomencaltura") ?: ""
        val trabajador = intent.getStringExtra("trabajador") ?: ""
        val hora = intent.getStringExtra("hora") ?: ""
        val tecnico = intent.getStringExtra("tecnico") ?: ""
        val entidad = intent.getStringExtra("entidad") ?: ""
        val servicio = intent.getStringExtra("servicio") ?: ""
        val mantenimiento = intent.getStringExtra("mantenimiento") ?: ""
        val fechaEmision = intent.getStringExtra("fechaEmision") ?: ""
        val fechaApertura = intent.getStringExtra("fechaApertura") ?: ""
        val fechaLlegada = intent.getStringExtra("fechaLlegada") ?: ""
        val fechaCierre = intent.getStringExtra("fechaCierre") ?: ""

        val cliente = "RED GUANAJUATO"

        val btnGenerar = findViewById<Button>(R.id.btnGenerarReporte)
        btnGenerar.setOnClickListener {
            try {
                val plantilla = copiarPlantillaDesdeAssets("plantilla2.docx")
                val salida = File(getExternalFilesDir(null), "reporte_generado.docx")

                val datos = mapOf(
                    "cliente" to cliente,
                    "municipio" to municipio,
                    "direccion" to direccion,
                    "codigo" to codigoPostal,
                    "nombre" to nombreSitio,
                    "espacio" to tipoEspacio,
                    "latitud" to latitud,
                    "longitud" to longitud,
                    "clave" to claveNodo,
                    "id" to nomencaltura,
                    "trabajador" to trabajador,
                    "hora" to hora,
                    "tecnico" to tecnico,
                    "entidad" to entidad,
                    "servicio" to servicio,
                    "mantenimiento" to mantenimiento,
                    "emision" to fechaEmision,
                    "apertura" to fechaApertura,
                    "llegada" to fechaLlegada,
                    "cierre" to fechaCierre
                )

                for ((clave, valor) in datos) {
                    Log.d("DATOS", "$clave = '$valor'")
                }

                generarReporteDesdePlantilla(plantilla, salida, datos)

                Toast.makeText(this, "Reporte generado.", Toast.LENGTH_SHORT).show()
                compartirPorWhatsapp(salida)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "Error generando reporte: ${e.message}", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun copiarPlantillaDesdeAssets(nombre: String): File {
        val archivo = File(filesDir, nombre)
        assets.open(nombre).use { input ->
            archivo.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        return archivo
    }

    private fun generarReporteDesdePlantilla(
        plantilla: File,
        salida: File,
        valores: Map<String, String>
    ) {
        val tempDir = File(salida.parentFile, "temp_docx")
        if (tempDir.exists()) tempDir.deleteRecursively()
        tempDir.mkdirs()

        // Descomprimir .docx
        ZipFile(plantilla).use { zip ->
            zip.entries().asSequence().forEach { entry ->
                val outFile = File(tempDir, entry.name)
                outFile.parentFile?.mkdirs()
                if (!entry.isDirectory) {
                    zip.getInputStream(entry).use { input ->
                        outFile.outputStream().use { output ->
                            input.copyTo(output)
                        }
                    }
                }
            }
        }

        val docXml = File(tempDir, "word/document.xml")
        var contenido = docXml.readText(Charsets.UTF_8)

        // 1. Unir todos los <w:t>...</w:t> bloques en uno temporal
        val bloques = Regex("<w:t[^>]*>(.*?)</w:t>").findAll(contenido).toList()

        val textoPlano = bloques.joinToString("") { it.groupValues[1] }

        // 2. Reemplazar marcadores en el texto completo
        val textoReemplazado = Regex("\\{\\{([a-zA-Z0-9_]+)\\}\\}").replace(textoPlano) { match ->
            val clave = match.groupValues[1]
            val valor = valores[clave] ?: ""
            Log.d("REEMPLAZO", "Reemplazando {{$clave}} por '$valor'")
            valor
        }

        // 3. Distribuir el texto reemplazado de vuelta en los bloques originales
        var offset = 0
        val nuevoContenido = StringBuilder(contenido)
        for (i in bloques.indices) {
            val match = bloques[i]
            val inicio = match.range.first + offset
            val fin = match.range.last + 1 + offset
            val nuevoTexto = textoReemplazado.substring(0, match.groupValues[1].length)
            textoReemplazado.drop(match.groupValues[1].length)
            val reemplazo = match.value.replace(match.groupValues[1], nuevoTexto)
            nuevoContenido.replace(inicio, fin, reemplazo)
            offset += reemplazo.length - match.value.length
        }

        docXml.writeText(nuevoContenido.toString(), Charsets.UTF_8)

        // Reempaquetar .docx
        ZipOutputStream(FileOutputStream(salida)).use { zos ->
            tempDir.walkTopDown().forEach { file ->
                if (file.isFile) {
                    val entryName = file.relativeTo(tempDir).path.replace("\\", "/")
                    val entry = ZipEntry(entryName)
                    zos.putNextEntry(entry)
                    file.inputStream().use { input ->
                        input.copyTo(zos)
                    }
                    zos.closeEntry()
                }
            }
        }

        tempDir.deleteRecursively()
    }


    private fun compartirPorWhatsapp(archivo: File) {
        val uri = FileProvider.getUriForFile(this, "$packageName.fileprovider", archivo)

        // Intento de compartir por WhatsApp
        val intentWhatsApp = Intent(Intent.ACTION_SEND).apply {
            type = "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
            putExtra(Intent.EXTRA_STREAM, uri)
            setPackage("com.whatsapp")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        if (intentWhatsApp.resolveActivity(packageManager) != null) {
            startActivity(intentWhatsApp)
        } else {
            // Si WhatsApp no está disponible, intentamos con correo
            val intentCorreo = Intent(Intent.ACTION_SEND).apply {
                type = "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
                putExtra(Intent.EXTRA_SUBJECT, "Reporte generado")
                putExtra(Intent.EXTRA_TEXT, "Adjunto el reporte solicitado.")
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            if (intentCorreo.resolveActivity(packageManager) != null) {
                startActivity(Intent.createChooser(intentCorreo, "Enviar reporte por correo"))
            } else {
                Toast.makeText(this, "No hay apps para compartir el archivo.", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }
}
