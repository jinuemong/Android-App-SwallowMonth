package com.jinuemong.SwallowMonthJM.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jinuemong.SwallowMonthJM.MainActivity
import com.jinuemong.SwallowMonthJM.Model.Profile
import com.jinuemong.SwallowMonthJM.Unit.getPhotoUrl
import com.jinuemong.SwallowMonthJM.databinding.ItemSearchUserBinding

class MiniProfileHoAdapter(
    private val mainActivity: MainActivity
) :RecyclerView.Adapter<MiniProfileHoAdapter.ViewHolder>(){
    private lateinit var binding : ItemSearchUserBinding
    private var itemList = ArrayList<Profile>()

    private var onItemClickListener : OnItemClickListener? = null
    interface OnItemClickListener{
        fun itemClick(profileId : Int)
    }
    fun setOnItemClickListener(listener: OnItemClickListener){
        this.onItemClickListener = listener
    }
    inner class ViewHolder(binding: ItemSearchUserBinding)
        :RecyclerView.ViewHolder(binding.root){
            fun hold(){
                val item = itemList[absoluteAdapterPosition]
                binding.userComment.text =item.userComment
                binding.userName.text = item.userName
                Glide.with(mainActivity)
                    .load(getPhotoUrl(item.userImage,mainActivity.masterApp.baseUrl))
                    .into(binding.userImage)

                binding.root.setOnClickListener {
                    onItemClickListener?.itemClick(item.profileId)
                }
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemSearchUserBinding.inflate(LayoutInflater.from(mainActivity),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.hold()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data : ArrayList<Profile>){
        this.itemList = data
        notifyDataSetChanged()
    }
}