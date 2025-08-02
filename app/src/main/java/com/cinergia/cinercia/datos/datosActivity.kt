package com.cinergia.cinercia.datos

import android.app.AlertDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.cinergia.cinercia.R
import com.cinergia.cinercia.fotos.fotosUnoActivity
import java.util.Calendar

class datosActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_datos)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
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

        findViewById<TextView>(R.id.tvNombreNodoD).text = "Nombre: " + nodoNombre
        findViewById<TextView>(R.id.tvTipoNodoD).text = "Tipo: " + nodoTipo
        findViewById<TextView>(R.id.tvMunicipioNodoD).text = "Municipio: " + nodoMunicipio
        findViewById<TextView>(R.id.tvComunidadNodoD).text = "Comunidad: " + nodoComunidad
        findViewById<TextView>(R.id.tvDireccionNodoD).text = "Dirreccion: " + nodoDireccion
        findViewById<TextView>(R.id.tvTelefonoNodoD).text = "Telefono: " + nodoTelefono
        findViewById<TextView>(R.id.tvCorreoNodoD).text = "Correo: " + nodoCorreo

        val tvFechaEmision = findViewById<TextView>(R.id.tvFechaEmisionD)
        val tvFechaApertura = findViewById<TextView>(R.id.tvFechaAperturaD)
        val tvFechaLlegada = findViewById<TextView>(R.id.tvFechaLlegadaD)
        val tvFechaCierre = findViewById<TextView>(R.id.tvFechaCierreD)

        val sEmpleado = findViewById<Spinner>(R.id.sEmpleados)
        val sArea = findViewById<Spinner>(R.id.sTecnico)
        val sEntidad = findViewById<Spinner>(R.id.sEntidad)
        val sServicio = findViewById<Spinner>(R.id.sServicio)
        val sMantenimiento = findViewById<Spinner>(R.id.sMantenimiento)
        val tvHora = findViewById<TextView>(R.id.tvHora)

        val btnGoToFotos1 = findViewById<Button>(R.id.btnGoToFotos)

        val empleados = listOf(" ", "Jaime López Horta", "Juan Manuel Manríquez Sarabia", "Ricardo Garcidueñas Vargas")
        val areaTecnio = listOf(" ", "Jardín", "Kiosco", "Presidencia ", "Dirección ", "Patio ", "Aula ", "Biblioteca")
        val entidad = listOf(" ", "Publico", "Preescolar", "Primaria", "Telesecundaria", "SABES", "UVEG", "Sitio de Repeticion")
        val servicio = listOf(" ", "Servicio Preventivo", "Servicio Correctivo", "Servicio Diagnostico")
        val mantenimineto = listOf(" ", "Mantenimiento Preventivo", "Mantenimiento Correctivo", "Mantenimineto de Diagnostico")

        // Adaptadores Para los spinner
        // Empleados
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, empleados)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sEmpleado.adapter = adapter

        sEmpleado.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val empleadoSeleccionado = empleados[position]
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        //Area
        val adapterArea = ArrayAdapter(this, android.R.layout.simple_spinner_item, areaTecnio)
        adapterArea.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sArea.adapter = adapterArea

        sArea.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val areaSeleccionado = areaTecnio[position]
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        //Entidad
        val adapterEntidad = ArrayAdapter(this, android.R.layout.simple_spinner_item, entidad)
        adapterEntidad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sEntidad.adapter = adapterEntidad

        sEntidad.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val entidadSeleccionado = entidad[position]
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        //Servicio
        val adapterServicio = ArrayAdapter(this, android.R.layout.simple_spinner_item, servicio)
        adapterServicio.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sServicio.adapter = adapterServicio

        sServicio.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val servicioSeleccionado = servicio[position]
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        //Mantenimineto
        val adapterMantenimineto = ArrayAdapter(this, android.R.layout.simple_spinner_item, mantenimineto)
        adapterMantenimineto.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sMantenimiento.adapter = adapterMantenimineto

        sMantenimiento.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val manteniminetoSeleccionado = mantenimineto[position]
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // Hora
        tvHora.setOnClickListener {
            val calendar = Calendar.getInstance()
            val horaActual = calendar.get(Calendar.HOUR_OF_DAY)
            val minutoActual = calendar.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(
                this,
                { _, horaSeleccionada, minutoSeleccionado ->
                    val horaFormato = String.format("%02d:%02d  ", horaSeleccionada, minutoSeleccionado)
                    tvHora.text = horaFormato
                },
                horaActual,
                minutoActual,
                false
            )

            timePickerDialog.show()
        }

        // FechaEmision
        tvFechaEmision.setOnClickListener {
            val calendario = Calendar.getInstance()
            val year = calendario.get(Calendar.YEAR)
            val month = calendario.get(Calendar.MONTH)
            val day = calendario.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog =
                android.app.DatePickerDialog(this, { _, yearSel, monthSel, daySel ->
                    val fechaSeleccionada =
                        String.format("%02d/%02d/%04d", daySel, monthSel + 1, yearSel)
                    tvFechaEmision.text = "$fechaSeleccionada"
                }, year, month, day)

            datePickerDialog.show()
        }

        // FechaApertura
        tvFechaApertura.setOnClickListener {
            val calendario = Calendar.getInstance()
            val year = calendario.get(Calendar.YEAR)
            val month = calendario.get(Calendar.MONTH)
            val day = calendario.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog =
                android.app.DatePickerDialog(this, { _, yearSel, monthSel, daySel ->
                    val fechaSeleccionada =
                        String.format("%02d/%02d/%04d", daySel, monthSel + 1, yearSel)
                    tvFechaApertura.text = "$fechaSeleccionada"
                }, year, month, day)

            datePickerDialog.show()
        }

        //FechaLlegada
        tvFechaLlegada.setOnClickListener {
            val calendario = Calendar.getInstance()
            val year = calendario.get(Calendar.YEAR)
            val month = calendario.get(Calendar.MONTH)
            val day = calendario.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog =
                android.app.DatePickerDialog(this, { _, yearSel, monthSel, daySel ->
                    val fechaSeleccionada =
                        String.format("%02d/%02d/%04d", daySel, monthSel + 1, yearSel)
                    tvFechaLlegada.text = "$fechaSeleccionada"
                }, year, month, day)

            datePickerDialog.show()
        }

        //FechaCierre
        tvFechaCierre.setOnClickListener {
            val calendario = Calendar.getInstance()
            val year = calendario.get(Calendar.YEAR)
            val month = calendario.get(Calendar.MONTH)
            val day = calendario.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog =
                android.app.DatePickerDialog(this, { _, yearSel, monthSel, daySel ->
                    val fechaSeleccionada =
                        String.format("%02d/%02d/%04d", daySel, monthSel + 1, yearSel)
                    tvFechaCierre.text = "$fechaSeleccionada"
                }, year, month, day)

            datePickerDialog.show()
        }


        btnGoToFotos1.setOnClickListener {
            // Valores
            val empleado = sEmpleado.selectedItem.toString()
            val area = sArea.selectedItem.toString()
            val Entidad = sEntidad.selectedItem.toString()
            val Servicio = sServicio.selectedItem.toString()
            val mantenimiento = sMantenimiento.selectedItem.toString()

            val hora = tvHora.text.toString()
            val fechaEmision = tvFechaEmision.text.toString()
            val fechaApertura = tvFechaApertura.text.toString()
            val fechaLlegada = tvFechaLlegada.text.toString()
            val fechaCierre = tvFechaCierre.text.toString()

            if (empleado.isEmpty() || area.isEmpty() || Entidad.isEmpty() || Servicio.isEmpty() ||
                mantenimiento.isEmpty() || hora.isEmpty() || fechaEmision.isEmpty() ||
                fechaApertura.isEmpty() || fechaLlegada.isEmpty() || fechaCierre.isEmpty()) {

                Toast.makeText(this, "Por favor completa todos los campos antes de continuar", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            AlertDialog.Builder(this)
                .setTitle("Confirmación")
                .setMessage("¿Seguro que quieres continuar?")
                .setPositiveButton("Sí") { _, _ ->
                    val intent = Intent(this, fotosUnoActivity::class.java).apply {
                        putExtra("nodoId", nodoId)
                        putExtra("nodoNombre", nodoNombre)
                        putExtra("nodoMunicipio", nodoMunicipio)
                        putExtra("nodoComunidad", nodoComunidad)
                        putExtra("nodoDireccion", nodoDireccion)
                        putExtra("nodoTipo", nodoTipo)
                        putExtra("nodoLatitud", nodoLatitud)
                        putExtra("nodoLongitud", nodoLongitud)
                        putExtra("nodoTelefono", nodoTelefono)
                        putExtra("nodoCorreo", nodoCorreo)
                        putExtra("nodoClaveNodo", nodoClaveNodo)
                        putExtra("nodoCodigoPostal", nodoCodigoPostal)
                        putExtra("nodoNomenclatura", nodoNomenclatura)

                        putExtra("empleado", empleado)
                        putExtra("area", area)
                        putExtra("entidad", Entidad)
                        putExtra("servicio", Servicio)
                        putExtra("mantenimiento", mantenimiento)
                        putExtra("hora", hora)
                        putExtra("fechaEmision", fechaEmision)
                        putExtra("fechaApertura", fechaApertura)
                        putExtra("fechaLlegada", fechaLlegada)
                        putExtra("fechaCierre", fechaCierre)
                    }
                    startActivity(intent)

                    sEmpleado.setSelection(0)
                    sArea.setSelection(0)
                    sEntidad.setSelection(0)
                    sServicio.setSelection(0)
                    sMantenimiento.setSelection(0)

                    tvHora.text = "Seleccionar hora"
                    tvFechaEmision.text = "Seleccionar fecha"
                    tvFechaApertura.text = "Seleccionar fecha"
                    tvFechaLlegada.text = "Seleccionar fecha"
                    tvFechaCierre.text = "Seleccionar fecha"

                }
                .setNegativeButton("Cancelar", null)
                .show()
        }
    }
}