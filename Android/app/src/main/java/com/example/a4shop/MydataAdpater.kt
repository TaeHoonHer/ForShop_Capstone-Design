package com.example.a4shop

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.a4shop.databinding.DataItemsBinding

class MydataAdapter(val MDArray: MutableList<MDcontents>, val onClick: (MDcontents) -> (Unit)) : RecyclerView.Adapter<MydataAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: DataItemsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(content : MDcontents) {
            // 촬영과 동시에 반환된 비율 데이터값 클래스에 연동
            binding.headData.setText(content.hdata.toString() + "%")
            binding.upData.setText(content.udata.toString() + "%")
            binding.downData.setText(content.ddata.toString() + "%")
            //binding.imgData.setImageResource()

            binding.dtCheck.setOnClickListener {
                onClick(content)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MydataAdapter.ViewHolder {
        return ViewHolder(
            DataItemsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MydataAdapter.ViewHolder, position: Int) {
        holder.bind(MDArray[position])
    }

    override fun getItemCount(): Int {
        return MDArray.size // 최근 생성된 4개 아이템 개수만큼 반환
    }
}