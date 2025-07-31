package com.cinergia.cinercia

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.Normalizer

class nodoAdapter(private var nodosOriginal: List<Nodo>, private val onItemClick: (Nodo) -> Unit) :
    RecyclerView.Adapter<nodoAdapter.NodoVieHolder>() {

    private var nodosFiltrados: List<Nodo> = nodosOriginal

    inner class NodoVieHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvNombreNodo: TextView = view.findViewById(R.id.tvNombreNodo)
        var tvMunicipioNodo: TextView = view.findViewById(R.id.tvMunicipioNodo)
        var tvIdNodo: TextView = view.findViewById(R.id.tvIdNodo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NodoVieHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_nodo, parent, false)
        return NodoVieHolder(view)
    }

    override fun onBindViewHolder(holder: NodoVieHolder, position: Int) {
        val nodo = nodosFiltrados[position]
        holder.tvNombreNodo.text = nodo.descripcion.nombre
        holder.tvMunicipioNodo.text = nodo.descripcion.municipio
        holder.tvIdNodo.text = nodo.id

        holder.itemView.setOnClickListener {
            onItemClick(nodo)
        }
    }

    override fun getItemCount(): Int = nodosFiltrados.size

    fun filtrar(texto: String) {
        val textoNormalizado = texto.normalizar()
        nodosFiltrados = if (textoNormalizado.isEmpty()) {
            nodosOriginal
        } else {
            nodosOriginal.filter {
                it.descripcion.nombre.normalizar().contains(textoNormalizado)
            }
        }
        notifyDataSetChanged()
    }

    fun updateData(newList: List<Nodo>) {
        nodosOriginal = newList
        nodosFiltrados = newList
        notifyDataSetChanged()
    }

    private fun String.normalizar(): String {
        val temp = Normalizer.normalize(this, Normalizer.Form.NFD)
        return Regex("\\p{InCombiningDiacriticalMarks}+").replace(temp, "").lowercase()
    }
}
