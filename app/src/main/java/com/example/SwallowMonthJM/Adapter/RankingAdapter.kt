package com.example.SwallowMonthJM.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.SwallowMonthJM.MainActivity
import com.example.SwallowMonthJM.Model.RecordData
import com.example.SwallowMonthJM.R
import com.example.SwallowMonthJM.databinding.ItemMonthDataHoBinding

class RankingAdapter (
    private val mainActivity: MainActivity,
    private var itemList : ArrayList<RecordData>,
    private val myName : String,
) : RecyclerView.Adapter<RankingAdapter.ViewHolder>(){
    private lateinit var binding : ItemMonthDataHoBinding
    var myPosition  = -1
    inner class ViewHolder(binding : ItemMonthDataHoBinding)
        :RecyclerView.ViewHolder(binding.root){
        @SuppressLint("SetTextI18n")
        fun hold(){
            val item = itemList[absoluteAdapterPosition]
            binding.userRanking.text = " ${item.ranking} "
            binding.userName.text = item.userId
            binding.totalPointMent.text = "${item.totalPoint} point!"
            binding.taskListPer.progress = item.totalPer
            binding.taskListPerText.text ="${item.totalPer}%"
            binding.totalActivity.text ="${item.clearNum} / ${item.activityNum}"

            if (myName==item.userId){
                myPosition = absoluteAdapterPosition
                binding.linear.setBackgroundResource(R.color.color_type1)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemMonthDataHoBinding.inflate(LayoutInflater.from(mainActivity),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.hold()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newData : ArrayList<RecordData>){
        itemList = newData
        notifyDataSetChanged()

    }

}