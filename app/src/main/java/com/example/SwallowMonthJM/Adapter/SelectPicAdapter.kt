package com.example.SwallowMonthJM.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.SwallowMonthJM.MainActivity
import com.example.SwallowMonthJM.R
import com.example.SwallowMonthJM.databinding.ItemOnePicBinding

class SelectPicAdapter(
    private val mainActivity: MainActivity,
    private val itemList: ArrayList<String>,
) : RecyclerView.Adapter<SelectPicAdapter.SelectPicViewHolder>() {

    private lateinit var binding: ItemOnePicBinding
    private var onItemClickListener :OnItemClickListener?=null
    private var selectedPicNum = -1
    interface OnItemClickListener{
        fun onItemClick(imageUri:String){
        }
    }
    fun setOnItemClickListener(listener: OnItemClickListener){
        this.onItemClickListener = listener
    }

    inner class SelectPicViewHolder(private val binding: ItemOnePicBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("NotifyDataSetChanged")
        fun bind(){
            val item = itemList[absoluteAdapterPosition]

            //카메라 뷰  = 0
            if (absoluteAdapterPosition==0){
                binding.checkImage.setImageResource(R.drawable.baseline_camera_alt_24)
                binding.checkBox.setImageResource(0) //카메라는 체크 박스 제거
            }else {
                Glide.with(mainActivity)
                    .load(item)
                    .into(binding.checkImage)
                if (selectedPicNum == absoluteAdapterPosition) {
                    binding.setCheck()
                } else {
                    binding.setUnCheck()
                }
            }
            binding.root.setOnClickListener {

                //카메라 세팅
                if (absoluteAdapterPosition ==0){
                    onItemClickListener?.onItemClick("camera!")

                //재 클릭 시 선택 종료
                } else if (absoluteAdapterPosition == selectedPicNum) {
                    onItemClickListener?.onItemClick("") //선택 uri 전달
                    selectedPicNum = -1

                //이미지 클릭 시 uri 전달
                } else {
                    onItemClickListener?.onItemClick(item) //선택 uri 전달
                    selectedPicNum = absoluteAdapterPosition
                }

                notifyDataSetChanged()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectPicViewHolder {
        binding = ItemOnePicBinding.inflate(LayoutInflater.from(mainActivity),parent,false)
        return SelectPicViewHolder(binding)
    }

    override fun getItemCount() = itemList.size

    override fun onBindViewHolder(holder: SelectPicViewHolder, position: Int) {
        holder.bind()
    }

    private fun ItemOnePicBinding.setCheck() =
        checkBox.setBackgroundResource(R.drawable.baseline_check_circle_24)

    private fun ItemOnePicBinding.setUnCheck() =
        checkBox.setBackgroundResource(R.drawable.circle)

}