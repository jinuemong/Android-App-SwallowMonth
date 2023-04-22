package com.jinuemong.SwallowMonthJM.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jinuemong.SwallowMonthJM.MainActivity
import com.jinuemong.SwallowMonthJM.Model.Routine
import com.jinuemong.SwallowMonthJM.Unit.calendarIcon
import com.jinuemong.SwallowMonthJM.databinding.ItemRepeatTaskBinding

//main 3번째 탭 : Routine List 불러오기

class RoutineListAdapter(
    private val mainActivity: MainActivity,
    private val dataSet : ArrayList<Routine>,

): RecyclerView.Adapter<RoutineListAdapter.RoutineListItemHolder>(){
    private lateinit var  binding :ItemRepeatTaskBinding
    private var itemList = dataSet

    private var onItemClickListener : OnItemClickListener?= null
    interface OnItemClickListener {
        fun onClick(item : Routine){

        }
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        this.onItemClickListener = listener
    }

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

                binding.root.setOnClickListener {
                    onItemClickListener?.onClick(item)
                }
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