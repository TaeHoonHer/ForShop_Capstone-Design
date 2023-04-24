package com.example.a4shop

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.a4shop.databinding.FilterItemsBinding
import com.example.a4shop.databinding.GalleryItemsBinding

class FilterAdapter(val FArray: MutableList<Fcontents>, val onClick: (Fcontents) -> (Unit)) : RecyclerView.Adapter<FilterAdapter.ViewHolder>() {
    private val latestKeywords: List<String>
    get() = FArray.takeLast(4).map { it.keyword }

    inner class ViewHolder(val binding: FilterItemsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(content : Fcontents) {
            binding.filterBtn.setText(content.keyword)

            if (content.keyword in latestKeywords) { // 최근 생성된 버튼이면 보이도록 처리
                binding.filterBtn.visibility = View.VISIBLE
            } else { // 그 외 버튼은 숨기도록 처리
                binding.filterBtn.visibility = View.GONE
            }

            binding.root.setOnClickListener {
                onClick(content)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterAdapter.ViewHolder {
        return ViewHolder(
            FilterItemsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: FilterAdapter.ViewHolder, position: Int) {
        holder.bind(FArray[position])
    }

    override fun getItemCount(): Int {
        return FArray.size // 최근 생성된 4개 아이템 개수만큼 반환
    }
}