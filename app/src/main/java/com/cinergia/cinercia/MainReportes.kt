package com.cinergia.cinercia

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cinergia.cinercia.datos.datosActivity
import com.cinergia.cinercia.service.AuthService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainReportes : AppCompatActivity() {

    private lateinit var rvNodos: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var adapter: nodoAdapter
    private var listaNodos: List<Nodo> = listOf()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_reportes)

        rvNodos = findViewById(R.id.rvNodos)
        searchView = findViewById(R.id.searchView)
        rvNodos.layoutManager = LinearLayoutManager(this)

        adapter = nodoAdapter(listOf()) { nodoSeleccionado ->
            val intent = Intent(this, datosActivity::class.java).apply {
                putExtra("nodoId", nodoSeleccionado.id)
                putExtra("nodoNombre", nodoSeleccionado.descripcion.nombre)
                putExtra("nodoMunicipio", nodoSeleccionado.descripcion.municipio)
                putExtra("nodoComunidad", nodoSeleccionado.descripcion.comunidad)
                putExtra("nodoDireccion", nodoSeleccionado.descripcion.direccion)
                putExtra("nodoTipo", nodoSeleccionado.descripcion.tipo)
                putExtra("nodoLatitud", nodoSeleccionado.descripcion.latitud)
                putExtra("nodoLongitud", nodoSeleccionado.descripcion.longitud)
                putExtra("nodoTelefono", nodoSeleccionado.descripcion.telefono)
                putExtra("nodoCorreo", nodoSeleccionado.descripcion.correo)
                putExtra("nodoClaveNodo", nodoSeleccionado.descripcion.claveNodo)
                putExtra("nodoCodigoPostal", nodoSeleccionado.descripcion.codigoPostal)
                putExtra("nodoNomenclatura", nodoSeleccionado.descripcion.nomenclatura)
            }
            startActivity(intent)
        }
        rvNodos.adapter = adapter

        lifecycleScope.launch {
            try {
                val nodos = withContext(Dispatchers.IO) {
                    AuthService.getNodos()
                }

                listaNodos = nodos
                Log.d("MainReportes", "Nodos cargados: ${listaNodos.size}")
                adapter.updateData(listaNodos)

            } catch (e: Exception) {
                Log.e("MainReportes", "Error al cargar nodos", e)
            }
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filtrar(newText.orEmpty())
                return true
            }
        })
    }
}



