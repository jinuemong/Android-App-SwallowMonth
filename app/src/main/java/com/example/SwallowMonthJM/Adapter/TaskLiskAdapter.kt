package com.example.SwallowMonthJM.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.SwallowMonthJM.MainActivity
import com.example.SwallowMonthJM.Unit.Task
import com.example.SwallowMonthJM.databinding.ItemTaskBinding

class TaskListAdapter(
    private val mainActivity: MainActivity,
    private val dataSet : ArrayList<Task>,
) : RecyclerView.Adapter<TaskListAdapter.TaskListItemHolder>(){
    private lateinit var binding : ItemTaskBinding

    private var onItemClickListener: OnItemClickListener?=null
    interface OnItemClickListener{
        fun onItemClick(item:Task,position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        this.onItemClickListener = listener
    }

    class TaskListItemHolder(val binding: ItemTaskBinding)
        : RecyclerView.ViewHolder(binding.root){
            fun bind(item:Task){

            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskListItemHolder {
        binding = ItemTaskBinding.inflate(LayoutInflater.from(mainActivity),parent,false)
        return TaskListItemHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskListItemHolder, position: Int) {
        holder.bind(dataSet[position])
    }

    override fun getItemCount(): Int =dataSet.size
}