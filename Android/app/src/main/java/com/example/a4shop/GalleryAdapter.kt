package com.example.a4shop

import android.content.ClipData
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Gallery
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.a4shop.databinding.GalleryItemsBinding

class GalleryAdapter(val GArray: MutableList<Gcontents>, val onClick: (Gcontents) -> (Unit)) : RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: GalleryItemsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(content : Gcontents) {
            binding.galleryImageview.setImageResource(content.image)

            binding.root.setOnClickListener {
                onClick(content)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryAdapter.ViewHolder {
        return ViewHolder(
            GalleryItemsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: GalleryAdapter.ViewHolder, position: Int) {
        holder.bind(GArray[position])
    }

    override fun getItemCount(): Int {
        return GArray.size
    }
}