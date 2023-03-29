package com.example.SwallowMonthJM.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.SwallowMonthJM.MainActivity
import com.example.SwallowMonthJM.Model.Alarm
import com.example.SwallowMonthJM.Model.AlarmForGet
import com.example.SwallowMonthJM.Unit.getPhotoUrl
import com.example.SwallowMonthJM.Unit.getTimeText
import com.example.SwallowMonthJM.databinding.ItemAlarmBinding

class AlarmListAdapter(
    private val mainActivity: MainActivity,
    dataSet : ArrayList<AlarmForGet>
) : RecyclerView.Adapter<AlarmListAdapter.ViewHolder>(){
    private lateinit var binding: ItemAlarmBinding
    private var itemSet = dataSet

    private var onItemClickListener:OnItemClickListener? =null
    interface OnItemClickListener{
        fun itemClick(type: String, profileId : Int)
    }
    fun setUpListener(listener: OnItemClickListener){
        this.onItemClickListener = listener
    }
    inner class ViewHolder(val binding : ItemAlarmBinding)
        :RecyclerView.ViewHolder(binding.root){
            @SuppressLint("SetTextI18n")
            fun bind(){
                val item  = itemSet[absoluteAdapterPosition]
                binding.timeText.text = getTimeText(item.alarm.createTime)
                Glide.with(mainActivity)
                    .load(getPhotoUrl(item.profile.userImage,(mainActivity.masterApp).baseUrl))
                    .into(binding.userImage)
                if (item.alarm.type =="FriendShip"){
                    binding.alarmText.text = "new friend request from ${item.profile.userName}"
                }
                onItemClickListener?.itemClick(item.alarm.type,item.profile.profileId)

            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemAlarmBinding.inflate(LayoutInflater.from(mainActivity)
        ,parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount()  =itemSet.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }


}