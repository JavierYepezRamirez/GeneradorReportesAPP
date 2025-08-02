package com.cinergia.cinercia.actividades

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cinergia.cinercia.EnviarActivity
import com.cinergia.cinercia.R
import com.cinergia.cinercia.service.AuthService

class actividadesActivity : AppCompatActivity() {
    private val actividadesSeleccionadas = mutableListOf<String>()
    private lateinit var adapter: ActividadAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_actividades)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val fotosTotales: ArrayList<Uri>? = intent.getParcelableArrayListExtra("fotos_totales")

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

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_actividades)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val searchView = findViewById<SearchView>(R.id.svActividades)

        adapter = ActividadAdapter(emptyList()) { seleccionada ->
            actividadesSeleccionadas.add(seleccionada)
        }
        recyclerView.adapter = adapter

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter(newText ?: "")
                return true
            }
        })

        val btnEnvar = findViewById<Button>(R.id.btnGoToEnviar)

        btnEnvar.setOnClickListener {
            val intent = Intent(this, EnviarActivity::class.java).apply {
                putStringArrayListExtra("actividades_seleccionadas", ArrayList(actividadesSeleccionadas))
                fotosTotales?.let {
                    putParcelableArrayListExtra("fotos_totales", it)
                }
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
                putExtra("entidad", entidad)
                putExtra("servicio", servicio)
                putExtra("mantenimiento", mantenimiento)
                putExtra("hora", hora)
                putExtra("fechaEmision", fechaEmision)
                putExtra("fechaApertura", fechaApertura)
                putExtra("fechaLlegada", fechaLlegada)
                putExtra("fechaCierre", fechaCierre)
            }
            startActivity(intent)

        }

        Thread {
            try {
                Log.d("Actividades", "Obteniendo actividades...")
                val actividades = AuthService.getActividades()
                Log.d("ActividadesCargadas", actividades.toString())
                runOnUiThread {
                    adapter.updateData(actividades)
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                    Log.e("ActividadesError", "Error al obtener actividades", e)
                }
            }
        }.start()

    }
}
