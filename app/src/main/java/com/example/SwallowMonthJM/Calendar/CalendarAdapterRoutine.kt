package com.example.SwallowMonthJM.Calendar

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.SwallowMonthJM.MainActivity
import com.example.SwallowMonthJM.Model.Routine
import com.example.SwallowMonthJM.R
import com.example.SwallowMonthJM.databinding.ItemCalendarBinding
import java.text.SimpleDateFormat
import java.util.*

//각 캘린더의 어댑터 (날짜 - 일모음 )
//루틴 데이터의 상세 정보 표시

class CalendarAdapterRoutine(
    private val mainActivity: MainActivity,
    private val calendarLayout: LinearLayout,
    date: Date,
    currentMonth: Int,
    private val routine: Routine,
    private val dayP : Int

) : RecyclerView.Adapter<CalendarAdapterRoutine.CalenderItemHolder>() {
    private lateinit var binding: ItemCalendarBinding


    //현재 캘린더 데이터 얻어옴
    private val dateDay: Int = SimpleDateFormat("dd", Locale.KOREA).format(date).toInt()
    private val dateMonth: Int = SimpleDateFormat("MM", Locale.KOREA).format(date).toInt()

    // init calendar
    private var customCalendar: CustomCalendar =
        CustomCalendar(mainActivity,date, dateDay, currentMonth, dateMonth)
    private var startIndex =0
    private var endIndex=0
    private var selectedPosition=0

    //데이터 초기화
    init {
        customCalendar.initBaseCalendar()
        startIndex = customCalendar.prevTail
        endIndex = customCalendar.currentMaxDate + customCalendar.prevTail
        selectedPosition = customCalendar.currentIndex - startIndex
    }

    //데이터 자르기
    private var dataSet = customCalendar.dateList.subList(startIndex,endIndex)

    inner class CalenderItemHolder(val binding: ItemCalendarBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("NotifyDataSetChanged")
        fun bind() {

            //텍스트 표시
            binding.calendarText.text = dataSet[absoluteAdapterPosition].day.toString()


            val dayRoutine = routine.dayRoutinePost.find { it.dayIndex==absoluteAdapterPosition}

            if ( dayRoutine==null){
                binding.reset()
            }else{
                if (dataSet[absoluteAdapterPosition].day<dateDay){
                    if (dayRoutine.clear){
                        binding.setClear()
                    }else{
                        binding.setFail()
                    }
                }else if (dataSet[absoluteAdapterPosition].day==dateDay){
                    if (dayRoutine.clear){
                        binding.setClear()
                    } else{
                        binding.setCalendar()
                    }
                }
            }

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

    private fun ItemCalendarBinding.setCalendar() {
        itemLineNormal.visibility = View.VISIBLE
        itemLineNormal.setBackgroundResource(R.drawable.ic_baseline_event_repeat_24)
    }
    private fun ItemCalendarBinding.setClear() {
        itemLineNormal.visibility = View.VISIBLE
        itemLineNormal.setBackgroundResource(R.drawable.ic_iconmonstr_check_mark_13)
    }

    private fun ItemCalendarBinding.setFail() {
        itemLineNormal.visibility = View.VISIBLE
        itemLineNormal.setBackgroundResource(R.drawable.ic_baseline_close_24)
    }

    private fun ItemCalendarBinding.reset() {
        itemLineNormal.visibility = View.GONE
    }
}