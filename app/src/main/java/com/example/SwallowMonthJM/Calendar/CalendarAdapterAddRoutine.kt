package com.example.SwallowMonthJM.Calendar

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.SwallowMonthJM.MainActivity
import com.example.SwallowMonthJM.Model.DayData
import com.example.SwallowMonthJM.R
import com.example.SwallowMonthJM.Unit.CyclePickerDialog
import com.example.SwallowMonthJM.databinding.ItemCalendarBinding
import java.text.SimpleDateFormat
import java.util.*

//각 캘린더의 어댑터 (날짜 - 일모음 )
class CalendarAdapterAddRoutine(
    private val mainActivity: MainActivity,
    private val calendarLayout: LinearLayout,
    private val fm: FragmentManager,
    date: Date,
    currentMonth: Int,

    ) : RecyclerView.Adapter<CalendarAdapterAddRoutine.CalenderItemHolder>() {
    private lateinit var binding: ItemCalendarBinding
    private var dataSet: ArrayList<DayData> = arrayListOf()

    //현재 캘린더 데이터 얻어옴
    private val dateDay: Int = SimpleDateFormat("dd", Locale.KOREA).format(date).toInt()
    private val dateMonth: Int = SimpleDateFormat("MM", Locale.KOREA).format(date).toInt()

    // init calendar
    private var customCalendar: CustomCalendar =
        CustomCalendar(mainActivity,date, dateDay, currentMonth, dateMonth)

    init {
        customCalendar.initBaseCalendar()
        dataSet = customCalendar.dateList

    }

    private var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }

    var selectedNum = -1
    var currentMonthDay = customCalendar.currentMaxDate + customCalendar.prevTail

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

            //클릭 조건
            if (selectedNum == -1) {
                binding.reset()
                mainActivity.addViewModel.startNum = -1
            }

            if (selectedNum == absoluteAdapterPosition) {
                binding.setOneSelected()
                mainActivity.addViewModel.startNum = selectedNum
            } else {
                binding.reset()
            }

            if (selectedNum!=-1 && mainActivity.addViewModel.cycle!=99) {
                if (absoluteAdapterPosition in selectedNum until currentMonthDay) {

                    if ((absoluteAdapterPosition - selectedNum) % mainActivity.addViewModel.cycle == 0) {
                        binding.setCheck()
                    } else {
                        binding.reset()
                    }
                }

            }

            //오늘 날짜
            if (dataSet[absoluteAdapterPosition].isSelected) {
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

            //클릭 이벤트
            if (onItemClickListener != null && dataSet[absoluteAdapterPosition].monthIndex == 0) {
                binding.root.setOnClickListener {
                    onItemClickListener?.onItemClick()
                    if(selectedNum==-1) {
                        selectedNum = absoluteAdapterPosition
                        val remainingDays = currentMonthDay - absoluteAdapterPosition
                        val dig = CyclePickerDialog(mainActivity,remainingDays)
                        dig.show(fm, null)
                        dig.setOnClickedListener(object : CyclePickerDialog.ButtonClickListener {
                            override fun onClicked() {
                                notifyDataSetChanged()
                            }
                        })
                    }else{

                        selectedNum = -1
                        mainActivity.addViewModel.keyData = ""
                        mainActivity.addViewModel.cycle = 99
                        mainActivity.addViewModel.totalRoutine = -1
                        mainActivity.addViewModel.topText = ""
                        notifyDataSetChanged()
                    }
                }
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

    private fun ItemCalendarBinding.setOneSelected() {
        itemLineNormal.visibility = View.VISIBLE
        itemLineNormal.setBackgroundResource(R.drawable.task_line_circle)
    }

    private fun ItemCalendarBinding.setCheck() {
        itemLineNormal.visibility = View.VISIBLE
        itemLineNormal.setBackgroundResource(R.drawable.ic_iconmonstr_check_mark_3)
    }

    private fun ItemCalendarBinding.reset() {
        itemLineNormal.visibility = View.GONE
    }

    private fun ItemCalendarBinding.setToday() =
        calendarText.apply { setTextAppearance(R.style.todayText) }

    private fun ItemCalendarBinding.setUnToday() =
        calendarText.apply { setTextAppearance(R.style.unTodayText) }


    private fun ItemCalendarBinding.setOtherMonth() =
        calendarText.apply { setTextAppearance(R.style.grayColorText) }

    private fun ItemCalendarBinding.setUnOtherMonth() =
        calendarText.apply { setTextAppearance(R.style.strongColorText) }
}