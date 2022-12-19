package com.example.SwallowMonthJM.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.SwallowMonthJM.MainActivity
import com.example.SwallowMonthJM.Model.DayData
import com.example.SwallowMonthJM.R
import com.example.SwallowMonthJM.Unit.dayOfWeek
import com.example.SwallowMonthJM.databinding.ItemCalendarHorizontalBinding

class CalendarListAdapter(
    private val mainActivity: MainActivity,
    private val dataSet : ArrayList<DayData>,
    private val todayIndex:Int
): RecyclerView.Adapter<CalendarListAdapter.CalendarListItemHolder>(){
    private var selectedPosition = todayIndex

    private lateinit var binding: ItemCalendarHorizontalBinding
    private var onItemClickListener: OnItemClickListener?=null


    interface OnItemClickListener{
        fun onItemClick(item : DayData, position: Int)
    }
    fun setOnItemClickListener(listener: OnItemClickListener){
        this.onItemClickListener = listener
    }

    inner class CalendarListItemHolder(val binding: ItemCalendarHorizontalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DayData) {
            if(selectedPosition==absoluteAdapterPosition){
                binding.setChecked()
            }else{
                binding.setUnchecked()
            }
            if(onItemClickListener!=null){
                binding.root.setOnClickListener {
                    onItemClickListener?.onItemClick(item, position = absoluteAdapterPosition)
                    if (selectedPosition!=absoluteAdapterPosition){
                        binding.setChecked()
                        notifyItemChanged(selectedPosition)
                        selectedPosition = absoluteAdapterPosition
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarListItemHolder {
        binding = ItemCalendarHorizontalBinding.inflate(LayoutInflater.from(mainActivity),parent,false)
        return CalendarListItemHolder(binding)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: CalendarListItemHolder, position: Int) {
        holder.bind(dataSet[position])
        holder.binding.itemHoDayNum.text = dataSet[position].day.toString()
        holder.binding.itemHoDayText.setText(dayOfWeek[(position%7)])
    }

    override fun getItemCount(): Int =dataSet.size

    @SuppressLint("ResourceAsColor")
    private fun ItemCalendarHorizontalBinding.setChecked() =
        itemBack.setBackgroundColor(ContextCompat.getColor(mainActivity, R.color.color_type1))

    @SuppressLint("ResourceAsColor")
    private fun ItemCalendarHorizontalBinding.setUnchecked() =
        itemBack.setBackgroundColor(ContextCompat.getColor(mainActivity, R.color.color_type3))
}