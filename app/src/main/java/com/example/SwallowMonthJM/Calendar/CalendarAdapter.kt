package com.example.SwallowMonthJM.Calendar

import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.SwallowMonthJM.R
import com.example.SwallowMonthJM.databinding.ItemCalendarBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

//각 캘린더의 어댑터 (날짜 )
class CalendarAdapter(
    val context:Context,
    private val calendarLayout:LinearLayout,
    private val date: Date,
    private val currentMonth: Int,
) :RecyclerView.Adapter<CalendarAdapter.CalenderItemHolder>(){
    private var dataSet : ArrayList<Int> = arrayListOf()
    private var customCalendar :CustomCalendar = CustomCalendar(date)
    init {
        customCalendar.initBaseCalendar()
        dataSet = customCalendar.dateList
    }
    inner class CalenderItemHolder(val binding: ItemCalendarBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalenderItemHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_calendar,parent,false)
        return CalenderItemHolder(ItemCalendarBinding.bind(view))
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: CalenderItemHolder, position: Int) {
        //텍스트 표시
        holder.binding.calendarText.text = dataSet[position].toString()

        //각 아이템의 높이 지정
        holder.binding.root.layoutParams.height = calendarLayout.height/6

        //오늘 날짜는 진하게
        val dateDay : String = SimpleDateFormat("dd",Locale.KOREA).format(date)
        val dateMonth : Int = SimpleDateFormat("MM",Locale.KOREA).format(date).toInt()
        //오늘 날짜 + 현재 달인 경우만
        if (dataSet[position]==dateDay.toInt() && currentMonth ==dateMonth){
            holder.binding.calendarText.apply {
                setTypeface(this.typeface, Typeface.BOLD_ITALIC)
            }
        }

        val firstDateIndex  = customCalendar.prevTail
        val lastDateIndex = dataSet.size - customCalendar.nextHead-1
        //다른 달인 경우 회색으로 표시
        if (position<firstDateIndex || position>lastDateIndex){
            holder.binding.calendarText.apply {
                setTextAppearance(R.style.grayColorText)
            }

        }
    }

    override fun getItemCount(): Int =dataSet.size
}