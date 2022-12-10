package com.example.SwallowMonthJM.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.SwallowMonthJM.MainActivity
import com.example.SwallowMonthJM.Unit.Task
import com.example.SwallowMonthJM.Unit.calendarIcon
import com.example.SwallowMonthJM.databinding.ItemTaskBinding

class TaskListAdapter(
    private val mainActivity: MainActivity,
    private val dataSet : ArrayList<Task>?,
    private val type : String,
) : RecyclerView.Adapter<TaskListAdapter.TaskListItemHolder>(){
    private lateinit var binding : ItemTaskBinding

    private var itemList = dataSet ?: ArrayList<Task>()
    private var onItemClickListener: OnItemClickListener?=null
    interface OnItemClickListener{
        fun onItemClick(item:Task,position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        this.onItemClickListener = listener
    }

    inner class TaskListItemHolder(val binding: ItemTaskBinding)
        : RecyclerView.ViewHolder(binding.root){
            @SuppressLint("SetTextI18n")
            fun bind(item:Task){
                val per = item.per
                binding.taskPer.progress = per
                binding.taskPerText.text = "$per%"
                binding.taskIcon.setImageResource(calendarIcon[item.iconType])
                binding.taskText.text = item.text
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskListItemHolder {
        binding = ItemTaskBinding.inflate(LayoutInflater.from(mainActivity),parent,false)
        return TaskListItemHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskListItemHolder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount(): Int =itemList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(dataSet: ArrayList<Task>){
        itemList = dataSet
        notifyDataSetChanged()
    }
}