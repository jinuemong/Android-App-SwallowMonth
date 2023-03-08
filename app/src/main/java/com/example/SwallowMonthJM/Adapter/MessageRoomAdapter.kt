package com.example.SwallowMonthJM.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.SwallowMonthJM.MainActivity
import com.example.SwallowMonthJM.Model.FriendData
import com.example.SwallowMonthJM.databinding.ItemMessageUserBinding

class MessageRoomAdapter(
    private val mainActivity: MainActivity,
    private var dataSet : ArrayList<FriendData>,
):RecyclerView.Adapter<MessageRoomAdapter.ViewHolder>(){

    private lateinit var binding: ItemMessageUserBinding

    private var onItemClickListener : OnItemClickListener?= null
    interface OnItemClickListener{
        fun onItemClick(frId : Int)
    }
    fun setOnItemClickListener(listener: OnItemClickListener){
        this.onItemClickListener = listener
    }

    inner class ViewHolder(val binding:ItemMessageUserBinding)
        :RecyclerView.ViewHolder(binding.root){
        fun bind(){
            val item = dataSet[absoluteAdapterPosition]
            binding.userName.text = item.profile.userName
            binding.userComment.text = item.profile.userComment
            Glide.with(mainActivity)
                .load(item.profile.userImage)
                .into(binding.userImage)
            binding.messageName.text = item.friendData.name

            if (onItemClickListener!=null) {
                binding.root.setOnClickListener {
                    onItemClickListener?.onItemClick(item.friendData.frId)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemMessageUserBinding.inflate(LayoutInflater.from(mainActivity))
        return ViewHolder(binding)
    }

    override fun getItemCount()=dataSet.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }
}