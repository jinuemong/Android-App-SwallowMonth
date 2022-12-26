package com.example.SwallowMonthJM.Calendar

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.SwallowMonthJM.MainActivity
import com.example.SwallowMonthJM.Model.DayData
import com.example.SwallowMonthJM.Model.DayRoutine
import com.example.SwallowMonthJM.R
import com.example.SwallowMonthJM.databinding.ItemCalendarBinding
import java.text.SimpleDateFormat
import java.util.*

//각 캘린더의 어댑터 (날짜 - 일모음 )
class CalendarAdapterNormal(
    private val mainActivity: MainActivity,
    private val calendarLayout: LinearLayout,
    date: Date,
    currentMonth: Int,
    private val dayRoutine: DayRoutine?

) : RecyclerView.Adapter<CalendarAdapterNormal.CalenderItemHolder>() {
    private lateinit var binding: ItemCalendarBinding
    private var dataSet: ArrayList<DayData> = arrayListOf()

    //현재 캘린더 데이터 얻어옴
    private val dateDay: Int = SimpleDateFormat("dd", Locale.KOREA).format(date).toInt()
    private val dateMonth: Int = SimpleDateFormat("MM", Locale.KOREA).format(date).toInt()
    // init calendar
    var customCalendar: CustomCalendar = CustomCalendar(date, dateDay, currentMonth, dateMonth,mainActivity.viewModel.dateTime)
    init {
        customCalendar.initBaseCalendar()
        dataSet = customCalendar.dateList
    }

    inner class CalenderItemHolder(val binding: ItemCalendarBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("NotifyDataSetChanged")
        fun bind() {

            //텍스트 표시
            binding.calendarText.text = dataSet[absoluteAdapterPosition].day.toString()
            //각 아이템의 높이 지정
            val params =
                LinearLayout.LayoutParams(calendarLayout.width / 7, calendarLayout.height / 6)
            binding.root.layoutParams = params

            //오늘 날짜
            if (dataSet[absoluteAdapterPosition].isSelected) {
                binding.setToday()
            }else{
                binding.setUnToday()
            }

            //다른 달 회색
            if (dataSet[absoluteAdapterPosition].monthIndex != 0) {
                binding.setOtherMonth()
            }else{
                binding.setUnOtherMonth()
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalenderItemHolder {
        binding = ItemCalendarBinding.inflate(LayoutInflater.from(mainActivity))
        return CalenderItemHolder(binding)
    }

    override fun onBindViewHolder(holder: CalenderItemHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int = dataSet.size


    private fun ItemCalendarBinding.setToday() =
        calendarText.apply { setTextAppearance(R.style.todayText) }

    private fun ItemCalendarBinding.setUnToday() =
        calendarText.apply { setTextAppearance(R.style.unTodayText) }


    private fun ItemCalendarBinding.setOtherMonth() =
        calendarText.apply { setTextAppearance(R.style.grayColorText) }
    private fun ItemCalendarBinding.setUnOtherMonth() =
        calendarText.apply { setTextAppearance(R.style.strongColorText) }


}