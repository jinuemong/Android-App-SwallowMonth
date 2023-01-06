package com.example.SwallowMonthJM.Statistics

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.SwallowMonthJM.Calendar.CustomCalendar
import com.example.SwallowMonthJM.MainActivity
import com.example.SwallowMonthJM.Model.DayData
import com.example.SwallowMonthJM.R
import com.example.SwallowMonthJM.databinding.ItemCalendarBinding
import com.example.SwallowMonthJM.databinding.StatisticsTopViewBinding
import java.text.SimpleDateFormat
import java.util.*

class CalendarAdapterStatistics(
    private val mainActivity: MainActivity,
    private val calendarLayout : LinearLayout,
    date : Date,
    currentMonth : Int
) :RecyclerView.Adapter<CalendarAdapterStatistics.CalendarStatisticsHolder>(){

    private lateinit var binding: StatisticsTopViewBinding
    private var dataSet : ArrayList<DayData> = arrayListOf()

    //현재 캘린더 데이터 얻어옴
    private val dateDay: Int = SimpleDateFormat("dd", Locale.KOREA).format(date).toInt()
    private val dateMonth: Int = SimpleDateFormat("MM", Locale.KOREA).format(date).toInt()

    // init calendar
    private var customCalendar: CustomCalendar =
        CustomCalendar(date, dateDay, currentMonth, dateMonth, mainActivity.viewModel.dateTime)

    init {
        customCalendar.initBaseCalendar()
        dataSet = customCalendar.dateList

    }

    inner class CalendarStatisticsHolder(val binding: StatisticsTopViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind() {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarStatisticsHolder {
        binding = StatisticsTopViewBinding.inflate(LayoutInflater.from(mainActivity))
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