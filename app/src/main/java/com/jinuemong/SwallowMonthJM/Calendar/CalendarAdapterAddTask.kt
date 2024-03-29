package com.jinuemong.SwallowMonthJM.Calendar

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.jinuemong.SwallowMonthJM.MainActivity
import com.jinuemong.SwallowMonthJM.R
import com.jinuemong.SwallowMonthJM.databinding.ItemCalendarBinding
import java.text.SimpleDateFormat
import java.util.*

//각 캘린더의 어댑터 (날짜 - 일모음 )
// task 추가
class CalendarAdapterAddTask(
    private val mainActivity: MainActivity,
    private val calendarLayout: LinearLayout,
    date: Date,
    currentMonth: Int,

) : RecyclerView.Adapter<CalendarAdapterAddTask.CalenderItemHolder>() {
    private lateinit var binding: ItemCalendarBinding

    //현재 캘린더 데이터 얻어옴
    private val dateDay: Int = SimpleDateFormat("dd", Locale.KOREA).format(date).toInt()
    private val dateMonth: Int = SimpleDateFormat("MM", Locale.KOREA).format(date).toInt()


    // init calendar
    private var customCalendar: CustomCalendar =
        CustomCalendar(mainActivity,date, dateDay, currentMonth, dateMonth)


    //데이터 초기화
    init {
        customCalendar.initBaseCalendar()

    }
    //데이터 저장
    private val subIndex = customCalendar.prevTail
    private var dataSet = customCalendar.dateList

    private var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }

    var startNum = -1
    var endNum = -1

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
            //취소
            if (startNum==-1 && endNum==-1) {
                binding.reset()
                mainActivity.addViewModel.startNum=-1
                mainActivity.addViewModel.endNum=-1
            }
            //하나 선택
            if (startNum==absoluteAdapterPosition){
                if (endNum==-1) {
                    binding.setOneSelected()
                }else{
                    binding.setHead()
                }
                mainActivity.addViewModel.startNum=startNum-subIndex
            }else{
                binding.reset()
            }

            //범위 선택
            if (endNum==absoluteAdapterPosition){
                binding.setTail()
                mainActivity.addViewModel.endNum=endNum-subIndex
            }
            if (endNum!=-1 && startNum!=-1 &&
                absoluteAdapterPosition>startNum && absoluteAdapterPosition<endNum){
                binding.setMid()
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

            //클릭 이벤트
            if (onItemClickListener != null && dataSet[absoluteAdapterPosition].monthIndex==0) {
                binding.root.setOnClickListener {
                    onItemClickListener?.onItemClick()

                    if (startNum!=-1 && endNum!=-1){
                        mainActivity.addViewModel.keyData = ""
                        startNum = -1
                        endNum = -1
                    }else{
                        if (startNum==-1){
                            mainActivity.addViewModel.keyData = customCalendar.keyDate
                            startNum = absoluteAdapterPosition
                        }else if ( startNum!=-1 && endNum==-1){
                            if (absoluteAdapterPosition != startNum) {
                                endNum = absoluteAdapterPosition
                                if (startNum > endNum) {
                                    val temp = startNum
                                    startNum = endNum
                                    endNum = temp
                                }
                            }
                        }
                    }
                    notifyDataSetChanged()
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
        itemLineMid.visibility = View.VISIBLE
        itemLineMid.setBackgroundResource(R.drawable.task_line_circle)
    }

    private fun ItemCalendarBinding.setHead() {
        itemLineMid.visibility = View.VISIBLE
        itemLineMid.setBackgroundResource(R.drawable.task_line_start)
    }

    private fun ItemCalendarBinding.setMid() {
        itemLineMid.visibility = View.VISIBLE
        itemLineMid.setBackgroundResource(R.drawable.task_line_mid)
    }
    private fun ItemCalendarBinding.setTail() {
        itemLineMid.visibility = View.VISIBLE
        itemLineMid.setBackgroundResource(R.drawable.task_line_end)
    }

    private fun ItemCalendarBinding.reset() {
        itemLineMid.visibility = View.GONE
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