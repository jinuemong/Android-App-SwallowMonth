package com.jinuemong.SwallowMonthJM.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.jinuemong.SwallowMonthJM.MainActivity
import com.jinuemong.SwallowMonthJM.R
import com.jinuemong.SwallowMonthJM.Unit.calendarIcon
import com.jinuemong.SwallowMonthJM.databinding.ItemIconBinding

//아이콘 선택

class IconAdapter(
    private val mainActivity: MainActivity
) : RecyclerView.Adapter<IconAdapter.IconViewHolder>(){
    private var selectedPosition = -1

    private lateinit var binding: ItemIconBinding
    private var onItemClickListener : OnItemClickListener? = null

     interface OnItemClickListener{
        fun onItemClick(iconIndex : Int)
    }
    fun setOnItemClickListener(listener:OnItemClickListener){
        this.onItemClickListener = listener
    }
    inner class IconViewHolder(val binding: ItemIconBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(){
            binding.itemIconImage.setImageResource(calendarIcon[absoluteAdapterPosition])

            if (selectedPosition==absoluteAdapterPosition){
                binding.setSelected()
            }else{
                binding.setUnSelected()
            }

            if (onItemClickListener!=null){
                binding.root.setOnClickListener {
                    onItemClickListener?.onItemClick(iconIndex=absoluteAdapterPosition)
                    if (selectedPosition!=absoluteAdapterPosition){
                        binding.setSelected()
                        notifyItemChanged(selectedPosition)
                        selectedPosition = absoluteAdapterPosition
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IconViewHolder {
        binding = ItemIconBinding.inflate(LayoutInflater.from(mainActivity),parent,false)
        return IconViewHolder(binding)
    }

    override fun onBindViewHolder(holder: IconViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int = calendarIcon.size

    private fun ItemIconBinding.setSelected() =
        itemIconBack.setBackgroundColor(ContextCompat.getColor(mainActivity, R.color.color_type1))
    private fun ItemIconBinding.setUnSelected() =
        itemIconBack.setBackgroundResource(R.drawable.rect_border)
}

