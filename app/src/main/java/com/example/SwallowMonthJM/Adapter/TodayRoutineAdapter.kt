package com.example.SwallowMonthJM.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.SwallowMonthJM.MainActivity
import com.example.SwallowMonthJM.Model.Routine
import com.example.SwallowMonthJM.Unit.calendarIcon
import com.example.SwallowMonthJM.databinding.ItemRoutineBinding

class TodayRoutineAdapter(
private val mainActivity: MainActivity,
private val dataSet : ArrayList<Routine>,
dPosition:Int,
private val isDone : Boolean,
): RecyclerView.Adapter<TodayRoutineAdapter.TodayRoutineItemHolder>(){
    private lateinit var  binding : ItemRoutineBinding
    private var itemList = dataSet
    private var dayPosition =dPosition
    private var onItemClickListener: OnItemClickListener?=null
    private var itemHeight =0

    interface OnItemClickListener{
        fun onItemClick(dayPosition: Int, routine: Routine)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        this.onItemClickListener = listener
    }

    inner class TodayRoutineItemHolder(val binding: ItemRoutineBinding)
        :RecyclerView.ViewHolder(binding.root){
        @SuppressLint("SetTextI18n")
        fun bind(item: Routine){
            val dayRoutine = item.dayRoutinePost.find { it.dayIndex==dayPosition}
            if(dayRoutine!=null && (dayRoutine.clear==isDone)){
                binding.routineIcon.setImageResource(calendarIcon[item.iconType])
                binding.routineText.text = item.text

                binding.root.layoutParams.height = itemHeight
                binding.isView()
                if(onItemClickListener!=null){
                    binding.root.setOnClickListener{
                        onItemClickListener?.onItemClick(dayPosition, item)
                    }
                }
            }else{
                binding.root.layoutParams.height = 0
                binding.isUnView()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodayRoutineAdapter.TodayRoutineItemHolder {
        binding = ItemRoutineBinding.inflate(LayoutInflater.from(mainActivity),parent,false)
        itemHeight= binding.root.layoutParams.height
        return TodayRoutineItemHolder(binding)
    }

    override fun onBindViewHolder(holder: TodayRoutineAdapter.TodayRoutineItemHolder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount() = itemList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(dataSet:ArrayList<Routine>){
        itemList = dataSet
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setDayDate(dPosition:Int){
        dayPosition = dPosition
        notifyDataSetChanged()
    }

    private fun ItemRoutineBinding.isView() {routineLayout.visibility = View.VISIBLE}
    private fun ItemRoutineBinding.isUnView() {routineLayout.visibility = View.GONE}
}