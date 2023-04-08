package com.example.SwallowMonthJM.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.SwallowMonthJM.Calendar.CustomCalendar
import com.example.SwallowMonthJM.MainActivity
import com.example.SwallowMonthJM.Model.DayData
import com.example.SwallowMonthJM.R
import com.example.SwallowMonthJM.Unit.dayOfWeek
import com.example.SwallowMonthJM.databinding.ItemCalendarHorizontalBinding

// main 첫번째 탭 : task list  가로 뷰

class CalendarListAdapter(
    private val mainActivity: MainActivity,
    dataSet : ArrayList<DayData>,
    customCalendar: CustomCalendar,
): RecyclerView.Adapter<CalendarListAdapter.CalendarListItemHolder>(){
    private var calendarData = customCalendar
    private var startIndex = calendarData.prevTail
    private var endIndex = calendarData.currentMaxDate + calendarData.prevTail
    private var selectedPosition = calendarData.currentIndex - startIndex
    //데이터 자르기
    private var dataList = dataSet.subList(startIndex,endIndex)

    init {
        if (selectedPosition !in 0 until itemCount ||
                selectedPosition!=mainActivity.viewModel.currentDayPosition.value!!) {
            selectedPosition = 0
        }
    }
    private lateinit var binding: ItemCalendarHorizontalBinding
    private var onItemClickListener: OnItemClickListener?=null


    interface OnItemClickListener{
        fun onItemClick(item : DayData, position: Int)
    }
    fun setOnItemClickListener(listener: OnItemClickListener){
        this.onItemClickListener = listener
    }

    inner class CalendarListItemHolder(val binding: ItemCalendarHorizontalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DayData) {
            if (selectedPosition == absoluteAdapterPosition) {
                binding.setChecked()
            } else {
                binding.setUnchecked()
            }
            if (onItemClickListener != null) {
                binding.root.setOnClickListener {
                    onItemClickListener?.onItemClick(item, position = absoluteAdapterPosition)
                    if (selectedPosition != absoluteAdapterPosition) {
                        binding.setChecked()
                        notifyItemChanged(selectedPosition)
                        selectedPosition = absoluteAdapterPosition
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarListItemHolder {
        binding = ItemCalendarHorizontalBinding.inflate(LayoutInflater.from(mainActivity),parent,false)
        return CalendarListItemHolder(binding)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: CalendarListItemHolder, position: Int) {
        holder.bind(dataList[position])

        holder.binding.itemHoDayNum.text = dataList[position].day.toString()
        holder.binding.itemHoDayText.setText(dayOfWeek[((position+startIndex)%7)])
    }

    override fun getItemCount(): Int =dataList.size

    @SuppressLint("ResourceAsColor")
    private fun ItemCalendarHorizontalBinding.setChecked() =
        itemBack.setBackgroundColor(ContextCompat.getColor(mainActivity, R.color.color_type1))

    @SuppressLint("ResourceAsColor")
    private fun ItemCalendarHorizontalBinding.setUnchecked() =
        itemBack.setBackgroundColor(ContextCompat.getColor(mainActivity, R.color.color_type3))
}