package com.cinergia.cinercia.actividades

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.cinergia.cinercia.R

class ActividadAdapter(
    private val actividades: List<String>,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<ActividadAdapter.ActividadViewHolder>() {

    private val seleccionadas = mutableListOf<String>()

    inner class ActividadViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val texto = itemView.findViewById<TextView>(R.id.text_actividad)
        private val card = itemView.findViewById<CardView>(R.id.card_actividad)

        fun bind(actividad: String) {
            texto.text = actividad

            if (seleccionadas.contains(actividad)) {
                card.setCardBackgroundColor(Color.BLUE)
                texto.setTextColor(Color.WHITE)
            } else {
                card.setCardBackgroundColor(Color.WHITE)
                texto.setTextColor(Color.BLACK)
            }

            card.setOnClickListener {
                if (seleccionadas.contains(actividad)) {
                    seleccionadas.remove(actividad)
                } else {
                    seleccionadas.add(actividad)
                    onItemClick(actividad)
                }
                notifyItemChanged(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActividadViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_actividad, parent, false)
        return ActividadViewHolder(view)
    }

    override fun onBindViewHolder(holder: ActividadViewHolder, position: Int) {
        holder.bind(actividades[position])
    }

    override fun getItemCount(): Int = actividades.size
}
