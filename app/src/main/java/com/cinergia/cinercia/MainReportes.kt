package com.cinergia.cinercia


import android.os.Build
import android.os.Bundle
import android.widget.SearchView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

        // 1. Asigna un adapter vacío para evitar el error
        adapter = nodoAdapter(listOf())
        rvNodos.adapter = adapter

        // 2. Carga datos de forma asíncrona
        lifecycleScope.launch {
            listaNodos = withContext(Dispatchers.IO) {
                AuthService.getNodos()
            }
            // 3. Actualiza el adapter con los nuevos datos
            adapter.updateData(listaNodos)
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


