package com.example.a4shop

import android.content.ClipData
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Gallery
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.a4shop.databinding.GalleryRecyclerviewBinding

/*class GalleryAdapter(val GArray: MutableList<Gcontents>) : RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: GalleryRecyclerviewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(content : Gcontents) {
            binding.imageView.setImageResource(content.image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return GalleryAdapter(
            GalleryRecyclerviewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(GArray[position])
    }

    override fun getItemCount(): Int {
        return GArray?.size
    }

}*/