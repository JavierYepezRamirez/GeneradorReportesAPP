package com.cinergia.cinercia

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView


class nodoAdapter(private var nodos: List<Nodo>) : RecyclerView.Adapter<nodoAdapter.NodoVieHolder>(){
    inner class NodoVieHolder(view:  View) : RecyclerView.ViewHolder(view){
        var tvNombreNodo : TextView = view.findViewById(R.id.tvNombreNodo)
        var tvMunicipioNodo : TextView = view.findViewById(R.id.tvMunicipioNodo)
        var tvIdNodo : TextView = view.findViewById(R.id.tvIdNodo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NodoVieHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_nodo,parent,false)
        return NodoVieHolder(view)
    }

    override fun onBindViewHolder(holder: NodoVieHolder, position: Int) {
        val nodo = nodos[position]
        holder.tvNombreNodo.text = nodo.description.nombre
        holder.tvMunicipioNodo.text = nodo.description.municipio
        holder.tvIdNodo.text = nodo.id
    }


    override fun getItemCount(): Int = nodos.size

    fun filtrar(texto: String) {
        nodos = nodos.filter {
            it.description.nombre.contains(texto, ignoreCase = true)
        }
        notifyDataSetChanged()
    }
    fun updateData(newList: List<Nodo>) {
        nodos = newList
        notifyDataSetChanged()
    }

}