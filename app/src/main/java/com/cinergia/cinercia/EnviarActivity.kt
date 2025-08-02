package com.cinergia.cinercia

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import kotlin.system.exitProcess

private lateinit var progressBar: ProgressBar
private lateinit var statusIcon: ImageView

class EnviarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_enviar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val actividadesSeleccionadas = intent.getStringArrayListExtra("actividades_seleccionadas")

        val fotosTotales = intent.getParcelableArrayListExtra<Uri>("fotos_totales")
        Log.d("DebugEnviar", "Actividades: $actividadesSeleccionadas")
        Log.d("DebugEnviar", "Fotos: $fotosTotales")



        val nodoId = intent.getStringExtra("nodoId")
        val nodoNombre = intent.getStringExtra("nodoNombre")
        val nodoMunicipio = intent.getStringExtra("nodoMunicipio")
        val nodoComunidad = intent.getStringExtra("nodoComunidad")
        val nodoDireccion = intent.getStringExtra("nodoDireccion")
        val nodoTipo = intent.getStringExtra("nodoTipo")
        val nodoLatitud = intent.getStringExtra("nodoLatitud")
        val nodoLongitud = intent.getStringExtra("nodoLongitud")
        val nodoTelefono = intent.getStringExtra("nodoTelefono")
        val nodoCorreo = intent.getStringExtra("nodoCorreo")
        val nodoClaveNodo = intent.getStringExtra("nodoClaveNodo")
        val nodoCodigoPostal = intent.getStringExtra("nodoCodigoPostal")
        val nodoNomenclatura = intent.getStringExtra("nodoNomenclatura")

        val empleado = intent.getStringExtra("empleado")
        val area = intent.getStringExtra("area")
        val entidad = intent.getStringExtra("entidad")
        val servicio = intent.getStringExtra("servicio")
        val mantenimiento = intent.getStringExtra("mantenimiento")
        val hora = intent.getStringExtra("hora")
        val fechaEmision = intent.getStringExtra("fechaEmision")
        val fechaApertura = intent.getStringExtra("fechaApertura")
        val fechaLlegada = intent.getStringExtra("fechaLlegada")
        val fechaCierre = intent.getStringExtra("fechaCierre")

        val reporteEstus = "1"

        progressBar = findViewById(R.id.progressBar)
        statusIcon = findViewById(R.id.statusIcon)

        val btnEnviarServidor = findViewById<Button>(R.id.btnGenerarReporte)
        val btnFinal = findViewById<Button>(R.id.btnFinalizar)

        val tvTextoInf = findViewById<TextView>(R.id.tvTextoInf)

        btnFinal.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Confirmar finalización")
            builder.setMessage("¿Estás seguro de que deseas finalizar?")
            builder.setPositiveButton("Sí") { dialog, _ ->
                dialog.dismiss()
                finishAffinity()
                exitProcess(0)
            }
            builder.setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }

            val dialog = builder.create()
            dialog.show()
        }



        btnEnviarServidor.setOnClickListener {
            enviarDatosAlServidor(
                actividadesSeleccionadas,
                fotosTotales,
                nodoId,
                nodoNombre,
                nodoMunicipio,
                nodoComunidad,
                nodoDireccion,
                nodoTipo,
                nodoLatitud,
                nodoLongitud,
                nodoTelefono,
                nodoCorreo,
                nodoClaveNodo,
                nodoCodigoPostal,
                nodoNomenclatura,
                empleado,
                area,
                entidad,
                servicio,
                mantenimiento,
                hora,
                fechaEmision,
                fechaApertura,
                fechaLlegada,
                fechaCierre,
                reporteEstus,
                btnEnviarServidor,
                tvTextoInf,
            )
        }
    }

    private fun enviarDatosAlServidor(
        actividadesSeleccionadas: List<String>?,
        fotosTotales: List<Uri>?,
        nodoId: String?,
        nodoNombre: String?,
        nodoMunicipio: String?,
        nodoComunidad: String?,
        nodoDireccion: String?,
        nodoTipo: String?,
        nodoLatitud: String?,
        nodoLongitud: String?,
        nodoTelefono: String?,
        nodoCorreo: String?,
        nodoClaveNodo: String?,
        nodoCodigoPostal: String?,
        nodoNomenclatura: String?,

        empleado: String?,
        area: String?,
        entidad: String?,
        servicio: String?,
        mantenimiento: String?,
        hora: String?,
        fechaEmision: String?,
        fechaApertura: String?,
        fechaLlegada: String?,
        fechaCierre: String?,
        reporteEstus: String?,
        btnEnviarServidor: Button,
        tvTextoInf: TextView

        ) {
        val url = "https://model-malamute-real.ngrok-free.app/reportes"

        btnEnviarServidor.text = "Enviando..."
        btnEnviarServidor.isEnabled = false
        btnEnviarServidor.alpha = 0.5f
        tvTextoInf.text = "Enviando Informacion..."

        val client = OkHttpClient()
        val gson = Gson()

        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)

        if (actividadesSeleccionadas != null) {
            val actividadesJson = gson.toJson(actividadesSeleccionadas)
            builder.addFormDataPart("actividadesSeleccionadas", actividadesJson)
        }

        nodoId?.let { builder.addFormDataPart("nodoId", it) }
        nodoNombre?.let { builder.addFormDataPart("nodoNombre", it) }
        nodoMunicipio?.let { builder.addFormDataPart("nodoMunicipio", it) }
        nodoComunidad?.let { builder.addFormDataPart("nodoComunidad", it) }
        nodoDireccion?.let { builder.addFormDataPart("nodoDireccion", it) }
        nodoTipo?.let { builder.addFormDataPart("nodoTipo", it) }
        nodoLatitud?.let { builder.addFormDataPart("nodoLatitud", it) }
        nodoLongitud?.let { builder.addFormDataPart("nodoLongitud", it) }
        nodoTelefono?.let { builder.addFormDataPart("nodoTelefono", it) }
        nodoCorreo?.let { builder.addFormDataPart("nodoCorreo", it) }
        nodoClaveNodo?.let { builder.addFormDataPart("nodoClaveNodo", it) }
        nodoCodigoPostal?.let { builder.addFormDataPart("nodoCodigoPostal", it) }
        nodoNomenclatura?.let { builder.addFormDataPart("nodoNomenclatura", it) }

        empleado?.let { builder.addFormDataPart("empleado", it) }
        area?.let { builder.addFormDataPart("area", it) }
        entidad?.let { builder.addFormDataPart("entidad", it) }
        servicio?.let { builder.addFormDataPart("servicio", it) }
        mantenimiento?.let { builder.addFormDataPart("mantenimiento", it) }
        hora?.let { builder.addFormDataPart("hora", it) }
        fechaEmision?.let { builder.addFormDataPart("fechaEmision", it) }
        fechaApertura?.let { builder.addFormDataPart("fechaApertura", it) }
        fechaLlegada?.let { builder.addFormDataPart("fechaLlegada", it) }
        fechaCierre?.let { builder.addFormDataPart("fechaCierre", it) }
        reporteEstus?.let {builder.addFormDataPart("reporteEstus", it)}

        fotosTotales?.forEach { uri ->
            Log.d("FotosEnviadas", "Uri: $uri")
        }

        fotosTotales?.forEachIndexed { index, uri ->
            val inputStream = contentResolver.openInputStream(uri)
            val bytes = inputStream?.readBytes()
            inputStream?.close()
            if (bytes != null) {
                val mimeType = contentResolver.getType(uri) ?: "image/jpeg"
                val requestFile = bytes.toRequestBody(mimeType.toMediaTypeOrNull())
                builder.addFormDataPart("fotos", "foto$index.jpg", requestFile)
            }
        }

        Log.d("DebugEnviar", "Actividades seleccionadas JSON: ${gson.toJson(actividadesSeleccionadas)}")
        Log.d("DebugEnviar", "Fotos totales cantidad: ${fotosTotales?.size ?: 0}")
        val requestBody = builder.build()

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        Thread {
            try {
                val response = client.newCall(request).execute()
                runOnUiThread {
                    progressBar.visibility = View.GONE
                    statusIcon.visibility = View.VISIBLE
                    if (response.isSuccessful) {
                        Log.e("RespuestaServidor", "Código: ${response.code}")
                        statusIcon.setImageResource(R.drawable.ic_check)
                        btnEnviarServidor.text = "Correcto"
                        btnEnviarServidor.isEnabled = true
                        btnEnviarServidor.alpha = 1.0f
                        tvTextoInf.text = "Enviado Correcto..."

                    } else {
                        Log.e("RespuestaServidor", "Body: ${response.body?.string()}")
                        statusIcon.setImageResource(R.drawable.ic_close)
                        btnEnviarServidor.text = "Intentar"
                        btnEnviarServidor.isEnabled = true
                        btnEnviarServidor.alpha = 1.0f
                        tvTextoInf.text = "Error al Enviado..."

                    }
                }
            } catch (e: Exception) {
                runOnUiThread {
                    progressBar.visibility = View.GONE
                    statusIcon.visibility = View.VISIBLE
                    statusIcon.setImageResource(R.drawable.ic_close)
                    btnEnviarServidor.text = "Error"
                    btnEnviarServidor.isEnabled = true
                    btnEnviarServidor.alpha = 1.0f
                    tvTextoInf.text = "Error Fatal..."

                }
            }
        }.start()
    }
}