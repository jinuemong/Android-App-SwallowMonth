package com.example.SwallowMonthJM.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.SwallowMonthJM.MainActivity
import com.example.SwallowMonthJM.Model.RecordData
import com.example.SwallowMonthJM.databinding.ItemMonthDataBinding

class RecordAdapter(
    private val mainActivity: MainActivity,
    private val dataSet : ArrayList<RecordData>
): RecyclerView.Adapter<RecordAdapter.ViewHolder>(){
    private lateinit var binding : ItemMonthDataBinding


    inner class ViewHolder(binding: ItemMonthDataBinding)
        :RecyclerView.ViewHolder(binding.root){
            @SuppressLint("SetTextI18n")
            fun bind(){
                val item = dataSet[absoluteAdapterPosition]
                binding.keyDate.text = item.keyDate
                binding.taskListPer.progress = item.totalPer
                binding.taskListPerText.text = "${item.totalPer}%"
                binding.subject.text = "${item.userId}'s Record Card"
                binding.totalActivity.text = "${item.clearNum} / ${item.activityNum}"
                binding.totalPointMent.text = "${item.totalPoint} point !"
                binding.userRanking.text = item.ranking.toString()
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemMonthDataBinding.inflate(LayoutInflater.from(mainActivity),
        parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = dataSet.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }
}