package com.example.SwallowMonthJM.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.SwallowMonthJM.MainActivity
import com.example.SwallowMonthJM.Unit.Task
import com.example.SwallowMonthJM.Unit.calendarIcon
import com.example.SwallowMonthJM.databinding.ItemTaskBinding

class TaskListAdapter(
    private val mainActivity: MainActivity,
    private val dataSet : ArrayList<Task>?,
    private val isDone : Boolean,
) : RecyclerView.Adapter<TaskListAdapter.TaskListItemHolder>(){
    private lateinit var binding : ItemTaskBinding
    private var itemList = dataSet ?: ArrayList<Task>()
    private var onItemClickListener: OnItemClickListener?=null
    var taskCount = 0

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        this.onItemClickListener = listener
    }

    inner class TaskListItemHolder(val binding: ItemTaskBinding)
        : RecyclerView.ViewHolder(binding.root){
            @SuppressLint("SetTextI18n")
            fun bind(item:Task){
                if(item.isDone==isDone){
                    val per = item.per
                    binding.taskPer.progress = per
                    binding.taskPerText.text = "$per%"
                    binding.taskIcon.setImageResource(calendarIcon[item.iconType])
                    binding.taskText.text = item.text
                    binding.isView()
                    if (onItemClickListener!=null) {
                        binding.root.setOnClickListener {
                            onItemClickListener?.onItemClick(position=absoluteAdapterPosition)
                        }
                    }
                }else{
                    binding.root.layoutParams.height = 0
                    binding.isUnView()
                }

                if(!isDone){
                    if(!item.isDone){
                        taskCount+=1
                    }
                    if (absoluteAdapterPosition==itemList.size-1){
                        mainActivity.viewModel.taskCount.value = taskCount
                    }
                }
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

    private fun ItemTaskBinding.isView() {taskLayout.visibility = View.VISIBLE}
    private fun ItemTaskBinding.isUnView() {taskLayout.visibility = View.GONE}
}