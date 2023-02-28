package com.example.SwallowMonthJM.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.SwallowMonthJM.MainActivity
import com.example.SwallowMonthJM.Model.Profile
import com.example.SwallowMonthJM.databinding.ItemUserProfileBinding

class RecommendUserAdapter(
    private val mainActivity: MainActivity,
    private val dataList : ArrayList<Profile>
) : RecyclerView.Adapter<RecommendUserAdapter.RecommendUserViewHolder>(){
    private lateinit var binding : ItemUserProfileBinding

    inner class RecommendUserViewHolder(val binding:ItemUserProfileBinding)
        :RecyclerView.ViewHolder(binding.root){
            fun bind(){
                val item = dataList[absoluteAdapterPosition]
                binding.userName.text = item.userName
                binding.userComment.text = item.userComment
                Glide.with(mainActivity)
                    .load(item.userImage)
                    .into(binding.userImage)
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendUserViewHolder {
        binding = ItemUserProfileBinding.inflate(LayoutInflater.from(mainActivity),parent,false)
        return RecommendUserViewHolder(binding)
    }

    override fun getItemCount(): Int =dataList.size

    override fun onBindViewHolder(holder: RecommendUserViewHolder, position: Int) {
        holder.bind()
    }

}