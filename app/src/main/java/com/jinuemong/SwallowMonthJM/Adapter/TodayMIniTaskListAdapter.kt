package com.jinuemong.SwallowMonthJM.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jinuemong.SwallowMonthJM.MainActivity
import com.jinuemong.SwallowMonthJM.Model.Routine
import com.jinuemong.SwallowMonthJM.Model.Task
import com.jinuemong.SwallowMonthJM.R
import com.jinuemong.SwallowMonthJM.Unit.calendarIcon
import com.jinuemong.SwallowMonthJM.databinding.ItemTodayTaskMiniBinding

//main 2번째 탭 Statistics :  task + routine 합한 뷰
class TodayMIniTaskListAdapter(
    private val mainActivity: MainActivity,
    private val dayPosition : Int,
    routineSet: ArrayList<Routine>?,
    taskSet: ArrayList<Task>?
) : RecyclerView.Adapter<TodayMIniTaskListAdapter.MIniTaskViewHolder>() {
    private lateinit var binding: ItemTodayTaskMiniBinding
    private var routineList = routineSet
    private var taskList = taskSet

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
            if (dayPosition==item.dayIndex) {
                if (item.isDone) {
                    binding.setChecked()
                } else {
                    binding.setUnChecked()
                }
                binding.textItemTodayTaskMini.text = item.text
                binding.iconItemTodayTaskMini.setImageResource(calendarIcon[item.iconType])

                if (onItemClickListener != null) {
                    binding.root.setOnClickListener {
                        onItemClickListener?.onItemClick(dayPosition, null, item)
                    }
                }
            }else{
                binding.root.layoutParams.height=0
            }
        }

        fun bindRoutine(item : Routine) {

            val dayRoutine = item.dayRoutinePost.find { it.dayIndex==dayPosition}
            if (dayRoutine!=null){
                if (dayRoutine.clear){
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
            }else{
                binding.root.layoutParams.height=0
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
            holder.bindRoutine(routineList!![position])
        }
    }

    override fun getItemCount(): Int {
        if (routineList==null && taskList==null){
            return 0
        }else{
            if( routineList==null){
                return taskList!!.size
            }else{
                return routineList!!.size
            }
        }
    }

    private fun ItemTodayTaskMiniBinding.setChecked() =
        checkboxItemTodayTaskMini.setBackgroundResource(R.drawable.ic_baseline_check_box_24)
    private fun ItemTodayTaskMiniBinding.setUnChecked() =
        checkboxItemTodayTaskMini.setBackgroundColor(R.drawable.ic_baseline_check_box_outline_blank_24)

    @SuppressLint("NotifyDataSetChanged")
    fun setRoutineData(data:ArrayList<Routine>?){
        if (data==null){
            routineList = ArrayList()
        }else {
            routineList = data
        }
        routineList = data
        notifyDataSetChanged()
    }
    @SuppressLint("NotifyDataSetChanged")
    fun setTaskData(data:ArrayList<Task>?){
        if (data==null){
            taskList = ArrayList()
        }else {
            taskList = data
        }
        notifyDataSetChanged()
    }
}