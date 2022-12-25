package com.example.SwallowMonthJM.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.SwallowMonthJM.MainActivity
import com.example.SwallowMonthJM.Model.Routine
import com.example.SwallowMonthJM.Unit.calendarIcon
import com.example.SwallowMonthJM.databinding.ItemRepeatTaskBinding

class RoutineListAdapter(
    private val mainActivity: MainActivity,
    private val dataSet : ArrayList<Routine>,

): RecyclerView.Adapter<RoutineListAdapter.RoutineListItemHolder>(){
    private lateinit var  binding :ItemRepeatTaskBinding
    private var itemList = dataSet

    inner class RoutineListItemHolder(val binding:ItemRepeatTaskBinding)
        :RecyclerView.ViewHolder(binding.root){
            @SuppressLint("SetTextI18n")
            fun bind(item:Routine){

                binding.routineCycle.text = item.topText
                binding.routineIcon.setImageResource(calendarIcon[item.iconType])
                binding.routineText.text = item.text
                val per = item.clearRoutine/item.totalRoutine
                binding.totalRoutine.text = "${item.clearRoutine} / ${item.totalRoutine}"
                binding.routinePerText.text = (per*100).toString()
                binding.routinePer.progress = per
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoutineListItemHolder {
        binding = ItemRepeatTaskBinding.inflate(LayoutInflater.from(mainActivity),parent,false)
        return RoutineListItemHolder(binding)
    }

    override fun onBindViewHolder(holder: RoutineListItemHolder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount() = itemList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(dataSet:ArrayList<Routine>){
        itemList = dataSet
        notifyDataSetChanged()
    }

}