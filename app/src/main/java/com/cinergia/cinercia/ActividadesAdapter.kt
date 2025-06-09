package com.cinergia.cinercia

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView

class ActividadesAdapter(
    private val lista: List<Actividad>
) : RecyclerView.Adapter<ActividadesAdapter.ActividadViewHolder>() {

    inner class ActividadViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val checkBox: CheckBox = itemView.findViewById(R.id.checkActividad)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActividadViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_todasactividades, parent, false)
        return ActividadViewHolder(view)
    }

    override fun onBindViewHolder(holder: ActividadViewHolder, position: Int) {
        val actividad = lista[position]
        holder.checkBox.text = actividad.nombre
        holder.checkBox.isChecked = actividad.seleccionada

        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            actividad.seleccionada = isChecked
        }
    }

    override fun getItemCount(): Int = lista.size

    fun obtenerSeleccionadas(): List<Actividad> {
        return lista.filter { it.seleccionada }
    }
}
