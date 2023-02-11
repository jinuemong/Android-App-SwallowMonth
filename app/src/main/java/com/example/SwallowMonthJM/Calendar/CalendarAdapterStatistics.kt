package com.example.SwallowMonthJM.Calendar

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.SwallowMonthJM.MainActivity
import com.example.SwallowMonthJM.R
import com.example.SwallowMonthJM.databinding.ItemCalendarBinding

class CalendarAdapterStatistics(
    private val mainActivity: MainActivity,
    private val calendarLayout : LinearLayout,
) :RecyclerView.Adapter<CalendarAdapterStatistics.CalendarStatisticsHolder>(){

    private lateinit var binding: ItemCalendarBinding

    private val dataSet = mainActivity.viewModel.currentDate.dateList
    private val subIndex = mainActivity.viewModel.currentDate.prevTail
    private val todayIndex = mainActivity.viewModel.todayDayPosition

    inner class CalendarStatisticsHolder(val binding: ItemCalendarBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind() {
            //텍스트 표시
            binding.calendarText.text = dataSet[absoluteAdapterPosition].day.toString()
            //각 아이템의 높이 지정
            val params =
                LinearLayout.LayoutParams(calendarLayout.width / 7, calendarLayout.height / 6)
            binding.root.layoutParams = params

            //오늘 날짜
            if (absoluteAdapterPosition==todayIndex) {
                binding.setToday()
            } else {
                binding.setUnToday()
            }

            //다른 달 회색
            if (dataSet[absoluteAdapterPosition].monthIndex != 0) {
                binding.setOtherMonth()
            } else {
                binding.setUnOtherMonth()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarStatisticsHolder {
        binding = ItemCalendarBinding.inflate(LayoutInflater.from(mainActivity))
        return  CalendarStatisticsHolder(binding)
    }

    override fun onBindViewHolder(holder: CalendarStatisticsHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int  = dataSet.size

    private fun ItemCalendarBinding.setToday() =
        calendarText.apply { setTextAppearance(R.style.todayText) }

    private fun ItemCalendarBinding.setUnToday() =
        calendarText.apply { setTextAppearance(R.style.unTodayText) }


    private fun ItemCalendarBinding.setOtherMonth() =
        calendarText.apply { setTextAppearance(R.style.grayColorText) }

    private fun ItemCalendarBinding.setUnOtherMonth() =
        calendarText.apply { setTextAppearance(R.style.strongColorText) }
}