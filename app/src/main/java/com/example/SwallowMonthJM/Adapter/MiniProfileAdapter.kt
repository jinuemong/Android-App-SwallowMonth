package com.example.SwallowMonthJM.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.SwallowMonthJM.MainActivity
import com.example.SwallowMonthJM.Model.FriendData
import com.example.SwallowMonthJM.Model.Profile
import com.example.SwallowMonthJM.Network.MasterApplication
import com.example.SwallowMonthJM.Unit.getPhotoUrl
import com.example.SwallowMonthJM.databinding.ItemUserProfileBinding

class MiniProfileAdapter(
    private val mainActivity: MainActivity,
    private val dataList : ArrayList<Profile>,
) : RecyclerView.Adapter<MiniProfileAdapter.MiniProfileViewHolder>(){
    private lateinit var binding : ItemUserProfileBinding

    private var onItemClickListener : OnItemClickListener?= null
    interface OnItemClickListener{
        fun onItemClick(item : Profile){}
    }
    fun setOnItemClickListener(listener: OnItemClickListener){
        this.onItemClickListener = listener
    }
    inner class MiniProfileViewHolder(val binding:ItemUserProfileBinding)
        :RecyclerView.ViewHolder(binding.root){
            fun bind(){
                val item = dataList[absoluteAdapterPosition]
                binding.userName.text = item.userName
                binding.userComment.text = item.userComment
                Glide.with(mainActivity)
                    .load(getPhotoUrl(item.userImage,(mainActivity.masterApp).baseUrl))
                    .into(binding.userImage)

                binding.root.setOnClickListener {
                    if (onItemClickListener!=null){
                        onItemClickListener?.onItemClick(item)
                    }
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MiniProfileViewHolder {
        binding = ItemUserProfileBinding.inflate(LayoutInflater.from(mainActivity),parent,false)
        return MiniProfileViewHolder(binding)
    }

    override fun getItemCount(): Int =dataList.size

    override fun onBindViewHolder(holder: MiniProfileViewHolder, position: Int) {
        holder.bind()
    }

}