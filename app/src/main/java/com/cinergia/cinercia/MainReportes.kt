package com.cinergia.cinercia

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.cinergia.cinercia.Generador.ReporteActivity

import java.io.File
import java.io.FileOutputStream

class MainReportes : AppCompatActivity() {

    private lateinit var btnFotos: Button
    private val rutasImagenes = ArrayList<String>()

    private val getMultipleImages =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                if (data?.clipData != null) {
                    // Selección múltiple
                    val count = data.clipData!!.itemCount
                    for (i in 0 until count) {
                        val imageUri = data.clipData!!.getItemAt(i).uri
                        guardarImagenLocalmente(imageUri)
                    }
                } else if (data?.data != null) {
                    // Selección única
                    guardarImagenLocalmente(data.data!!)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_reportes)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val sNodo = findViewById<Spinner>(R.id.sNodo)

        val listaNodos = leerNodosDesdeCSV(this)
        val nombresNodos = listaNodos.map { it.nodo }.distinct()

        val sTrabajador = findViewById<Spinner>(R.id.sTrabajador)
        val sHora = findViewById<Spinner>(R.id.sHora)
        val sTecnico = findViewById<Spinner>(R.id.sArea)
        val sEntida = findViewById<Spinner>(R.id.sEntidad)
        val sServicio = findViewById<Spinner>(R.id.sServicio)
        val sMantenimiento = findViewById<Spinner>(R.id.sMantenimiento)

        val etFEmision = findViewById<EditText>(R.id.etFechaEmision)
        val etFApertura = findViewById<EditText>(R.id.etFechaApertura)
        val etFLlegada = findViewById<EditText>(R.id.etFechaLlegada)
        val etFCierre = findViewById<EditText>(R.id.etFechaCierre)

        val btnActividades = findViewById<Button>(R.id.btnActividades)
        val btnAceptar = findViewById<Button>(R.id.btnAceptar)
        btnFotos = findViewById(R.id.btnFotos)

        val trabajadoresList = listOf(
            "",
            "Jaime López Horta",
            "Juan Manuel Manríquez Sarabia",
            "Ricardo Garcidueñas Vargas"
        )
        val horasList = listOf(
            "",
            "08:00 AM",
            "08:30 AM",
            "09:00 AM",
            "09:30 AM",
            "10:00 AM",
            "10:30 AM",
            "11:00 AM",
            "11:30 AM",
            "12:00 PM",
            "12:30 PM",
            "13:00 PM",
            "13:30 PM",
            "14:00 PM",
            "14:30 PM",
            "15:00 PM",
            "15:30 PM",
            "16:00 PM"
        )
        val tecnicosList = listOf(
            "",
            "Jardín",
            "Kiosco",
            "Presidencia ",
            "Dirección ",
            "Patio ",
            "Aula ",
            "Biblioteca"
        )
        val entidadesList =
            listOf("", "Publico", "Preescolar", "Primaria", "Telesecundaria", "SABES", "UVEG")
        val serviciosList = listOf("", "Servicio Preventivo", "Servicio Correctivo")
        val mantenimientoList = listOf("", "Mantenimiento Preventivo", "Mantenimiento Correctivo")

        fun <T> Spinner.setSimpleAdapter(context: Context, items: List<T>) {
            val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, items)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            this.adapter = adapter
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, nombresNodos)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sNodo.adapter = adapter

        var datosNodoSeleccionado: NodoData? = null

        sTrabajador.setSimpleAdapter(this, trabajadoresList)
        sHora.setSimpleAdapter(this, horasList)
        sTecnico.setSimpleAdapter(this, tecnicosList)
        sEntida.setSimpleAdapter(this, entidadesList)
        sServicio.setSimpleAdapter(this, serviciosList)
        sMantenimiento.setSimpleAdapter(this, mantenimientoList)

        sNodo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val nodoSeleccionado = nombresNodos[position]
                datosNodoSeleccionado = listaNodos.find { it.nodo == nodoSeleccionado }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        btnFotos.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "image/*"
                putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            }
            getMultipleImages.launch(Intent.createChooser(intent, "Selecciona imágenes"))
        }

        btnActividades.setOnClickListener {
            // Aquí va tu lógica de actividades si la tienes
        }

        btnAceptar.setOnClickListener {
            val intent = Intent(this, ReporteActivity::class.java)

            datosNodoSeleccionado?.let { nodo ->
                intent.putExtra("nodo", nodo.nodo)
                intent.putExtra("municipio", nodo.municipio)
                intent.putExtra("direccion", nodo.direccion)
                intent.putExtra("codigoPostal", nodo.codigoPostal)
                intent.putExtra("nombreSitio", nodo.nombreSitio)
                intent.putExtra("tipoEspacio", nodo.tipoEspacio)
                intent.putExtra("latitud", nodo.latitud)
                intent.putExtra("longitud", nodo.longitud)
                intent.putExtra("claveNodo", nodo.claveNodo)
                intent.putExtra("nomencaltura", nodo.nomencaltura)
            }

            intent.putExtra("trabajador", sTrabajador.selectedItem.toString())
            intent.putExtra("hora", sHora.selectedItem.toString())
            intent.putExtra("tecnico", sTecnico.selectedItem.toString())
            intent.putExtra("entidad", sEntida.selectedItem.toString())
            intent.putExtra("servicio", sServicio.selectedItem.toString())
            intent.putExtra("mantenimiento", sMantenimiento.selectedItem.toString())
            intent.putExtra("fechaEmision", etFEmision.text.toString())
            intent.putExtra("fechaApertura", etFApertura.text.toString())
            intent.putExtra("fechaLlegada", etFLlegada.text.toString())
            intent.putExtra("fechaCierre", etFCierre.text.toString())

            intent.putStringArrayListExtra("rutasImagenes", rutasImagenes)

            startActivity(intent)
            finish()
        }
    }

    private fun guardarImagenLocalmente(uri: Uri) {
        try {
            val inputStream = contentResolver.openInputStream(uri)
            val fileName = "imagen_${System.currentTimeMillis()}.jpg"
            val file = File(getExternalFilesDir("imagenes"), fileName)
            file.parentFile?.mkdirs()

            val outputStream = FileOutputStream(file)
            inputStream?.copyTo(outputStream)

            inputStream?.close()
            outputStream.close()

            rutasImagenes.add(file.absolutePath)

            Toast.makeText(this, "Imagen guardada: ${file.absolutePath}", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error al guardar imagen", Toast.LENGTH_SHORT).show()
        }
    }
}
