package com.example.SwallowMonthJM.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.SwallowMonthJM.MainActivity
import com.example.SwallowMonthJM.Model.Routine
import com.example.SwallowMonthJM.Model.Task
import com.example.SwallowMonthJM.R
import com.example.SwallowMonthJM.Unit.calendarIcon
import com.example.SwallowMonthJM.databinding.ItemTodayTaskMiniBinding

class TodayMIniTaskListAdapter(
    private val mainActivity: MainActivity,
    private val dayPosition : Int,
    routineSet: ArrayList<Routine>?,
    taskSet: ArrayList<Task>?
) : RecyclerView.Adapter<TodayMIniTaskListAdapter.MIniTaskViewHolder>() {
    private lateinit var binding: ItemTodayTaskMiniBinding
    private val routineList = routineSet
    private val taskList = taskSet

    private var onItemClickListener : OnItemClickListener? = null

    interface OnItemClickListener{
        fun onItemClick(dayPosition: Int,routine:Routine?,task:Task?)
    }
    fun setOnItemClickListener(listener: OnItemClickListener){
        this.onItemClickListener = listener
    }

    inner class MIniTaskViewHolder(val binding: ItemTodayTaskMiniBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindTask(item : Task){
            if (item.isDone){
                binding.setChecked()
            }else{
                binding.setUnChecked()
            }
            binding.textItemTodayTaskMini.text = item.text
            binding.iconItemTodayTaskMini.setImageResource(calendarIcon[item.iconType])

            if (onItemClickListener!=null){
                binding.root.setOnClickListener{
                    onItemClickListener?.onItemClick(dayPosition,null,item)
                }
            }
        }

        fun bindRoutine(item : Routine) {
            if (item.dayRoutineList.containsKey(dayPosition)){
                if (item.dayRoutineList[dayPosition]!!.clear){
                    binding.setChecked()
                }else{
                    binding.setUnChecked()
                }
                binding.textItemTodayTaskMini.text = item.text
                binding.iconItemTodayTaskMini.setImageResource(calendarIcon[item.iconType])

                if (onItemClickListener!=null){
                    binding.root.setOnClickListener{
                        onItemClickListener?.onItemClick(dayPosition,item,null)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MIniTaskViewHolder {
        binding = ItemTodayTaskMiniBinding.inflate(LayoutInflater.from(mainActivity),parent,false)
        return MIniTaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MIniTaskViewHolder, position: Int) {
        if (routineList==null){
            holder.bindTask(taskList!![position])
        }else{
            holder.bindRoutine(routineList[position])
        }
    }

    override fun getItemCount() = routineList?.size ?: taskList!!.size

    private fun ItemTodayTaskMiniBinding.setChecked() =
        checkboxItemTodayTaskMini.setBackgroundResource(R.drawable.ic_baseline_check_box_24)
    private fun ItemTodayTaskMiniBinding.setUnChecked() =
        checkboxItemTodayTaskMini.setBackgroundColor(R.drawable.ic_baseline_check_box_outline_blank_24)

}