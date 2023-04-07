package com.example.SwallowMonthJM.Adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.SwallowMonthJM.MainActivity
import com.example.SwallowMonthJM.Model.Task
import com.example.SwallowMonthJM.Unit.calendarIcon
import com.example.SwallowMonthJM.databinding.ItemTaskBinding

// main 첫번째 탭 : task list 세로 뷰

class TaskListAdapter(
    private val mainActivity: MainActivity,
    private val dataSet : ArrayList<Task>,
    dPosition:Int,
    private val isDone : Boolean,
) : RecyclerView.Adapter<TaskListAdapter.TaskListItemHolder>(){
    private lateinit var binding : ItemTaskBinding
    private var itemList = dataSet
    private var onItemClickListener: OnItemClickListener?=null
    private var itemHeight = 0
    private var dayPosition =dPosition

    interface OnItemClickListener{
        fun onItemClick(task: Task)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        this.onItemClickListener = listener
    }

    inner class TaskListItemHolder(val binding: ItemTaskBinding)
        : RecyclerView.ViewHolder(binding.root){
            @SuppressLint("SetTextI18n")
            fun bind(){
                val item = itemList[absoluteAdapterPosition]

                if(item.isDone==isDone && item.dayIndex==dayPosition){
                    val per = item.per
                    binding.taskPer.progress = per
                    binding.taskPerText.text = "$per%"
                    binding.taskIcon.setImageResource(calendarIcon[item.iconType])
                    binding.taskText.text = item.text

                    binding.root.layoutParams.height = itemHeight
                    binding.isView()
                    if (onItemClickListener!=null) {
                        binding.root.setOnClickListener {
                            onItemClickListener?.onItemClick(item)
                        }
                    }

                }else{
                    binding.root.layoutParams.height = 0
                    binding.isUnView()
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskListItemHolder {
        binding = ItemTaskBinding.inflate(LayoutInflater.from(mainActivity),parent,false)
        itemHeight= binding.root.layoutParams.height
        return TaskListItemHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskListItemHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int =itemList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(dataSet:ArrayList<Task>){
        itemList = dataSet
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setDayDate(dPosition:Int){
        dayPosition = dPosition
        notifyDataSetChanged()
    }

    private fun ItemTaskBinding.isView() {taskLayout.visibility = View.VISIBLE}
    private fun ItemTaskBinding.isUnView() {taskLayout.visibility = View.GONE}
}