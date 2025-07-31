package com.cinergia.cinercia.Generador

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.cinergia.cinercia.R

class ReporteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reporte2)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

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
        }

        fun enviadatos() {
        }
    }
}
