package com.cinergia.cinercia.fotos

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.cinergia.cinercia.R

class FotoAdapter(private val fotos: MutableList<Uri>) : RecyclerView.Adapter<FotoAdapter.FotoViewHolder>() {

    var onEliminarClick: ((pos: Int) -> Unit)? = null

    class FotoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imageViewFoto)
        val btnEliminar: ImageButton = view.findViewById(R.id.btnEliminar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FotoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_foto, parent, false)
        return FotoViewHolder(view)
    }

    override fun onBindViewHolder(holder: FotoViewHolder, position: Int) {
        holder.imageView.setImageURI(fotos[position])
        holder.btnEliminar.setOnClickListener {
            onEliminarClick?.invoke(position)
        }
    }

    fun eliminarFoto(pos: Int) {
        if (pos in fotos.indices) {
            fotos.removeAt(pos)
            notifyItemRemoved(pos)
        }
    }

    override fun getItemCount(): Int = fotos.size
}
