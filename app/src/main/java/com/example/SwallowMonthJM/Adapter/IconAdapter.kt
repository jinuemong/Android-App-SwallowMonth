package com.example.SwallowMonthJM.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.SwallowMonthJM.R
import com.example.SwallowMonthJM.Unit.calendarIcon
import com.example.SwallowMonthJM.databinding.ItemIconBinding

class IconAdapter(
) : RecyclerView.Adapter<IconAdapter.IconViewHolder>(){
    class IconViewHolder(val binding: ItemIconBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IconViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_icon,parent,false)
        return IconViewHolder(ItemIconBinding.bind(view))
    }

    override fun onBindViewHolder(holder: IconViewHolder, position: Int) {

        holder.binding.itemIconImage.setImageResource(calendarIcon[position])
    }

    override fun getItemCount(): Int = calendarIcon.size

}

