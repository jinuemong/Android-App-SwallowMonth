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

//각 캘린더의 어댑터 (날짜 - 일모음 )
//루틴 데이터의 상세 정보 표시

class CalendarAdapterRoutine(
    private val mainActivity: MainActivity,
    private val calendarLayout : LinearLayout,
    private val routine: Routine,

    ) : RecyclerView.Adapter<CalendarAdapterRoutine.CalenderItemHolder>() {
    private lateinit var binding: ItemCalendarBinding

    private val dataSet = mainActivity.viewModel.currentDate.dateList
    private val subIndex = mainActivity.viewModel.currentDate.prevTail
    private val todayIndex = mainActivity.viewModel.todayDayPosition
    private val currentDate = mainActivity.viewModel.currentDate.keyDate
    private val routineDate = routine.keyDate

    inner class CalenderItemHolder(val binding: ItemCalendarBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("NotifyDataSetChanged")
        fun bind() {
            //텍스트 표시
            binding.calendarText.text = dataSet[absoluteAdapterPosition].day.toString()

            //각 아이템의 높이 지정
            val params =
                LinearLayout.LayoutParams(calendarLayout.width / 7, (calendarLayout.height / 6
                +binding.itemLineNormal.layoutParams.height) )
            binding.root.layoutParams = params

            val dayRoutine = routine.dayRoutinePost
                .find { it.dayIndex==(absoluteAdapterPosition-subIndex)}
            if ( dayRoutine==null){
                binding.reset()
            }else{
                //현재 데이터가 오늘 이전
                if ( (absoluteAdapterPosition-subIndex) <todayIndex){
                    if (dayRoutine.clear){ //클리어
                        binding.setClear()
                    }else{
                        binding.setFail() //실패
                    }
                //현재 데이터가 오늘
                }else if ((absoluteAdapterPosition-subIndex)==todayIndex){

                    if (dayRoutine.clear){ //클리어
                        binding.setClear()
                    } else{
                        binding.setCalendar() //아직
                    }
                }
            }

            //오늘 날짜
            if (routineDate==currentDate &&absoluteAdapterPosition==todayIndex) {
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