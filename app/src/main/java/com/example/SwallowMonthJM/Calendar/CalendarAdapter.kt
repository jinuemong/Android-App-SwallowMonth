package com.example.SwallowMonthJM.Calendar

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.SwallowMonthJM.MainActivity
import com.example.SwallowMonthJM.R
import com.example.SwallowMonthJM.Unit.DayData
import com.example.SwallowMonthJM.databinding.ItemCalendarBinding
import java.text.SimpleDateFormat
import java.util.*

//각 캘린더의 어댑터 (날짜 - 일모음 )
class CalendarAdapter(
    private val mainActivity: MainActivity,
    private val calendarLayout: LinearLayout,
    private val dateTime: String,
    date: Date,
    currentMonth: Int,
) : RecyclerView.Adapter<CalendarAdapter.CalenderItemHolder>() {
    private lateinit var binding:ItemCalendarBinding

    //오늘 데이터 얻어옴
    private val dateDay: Int = SimpleDateFormat("dd", Locale.KOREA).format(date).toInt()
    private val dateMonth: Int = SimpleDateFormat("MM", Locale.KOREA).format(date).toInt()
    private var dataSet: ArrayList<DayData> = arrayListOf()
    var customCalendar: CustomCalendar = CustomCalendar(date,dateDay,currentMonth,dateMonth)
    init {
        customCalendar.initBaseCalendar()
        dataSet = customCalendar.dateList
    }

    private var onItemClickListener: OnItemClickListener?=null
    interface OnItemClickListener{
        fun onItemClick(item: DayData, position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        this.onItemClickListener = listener
    }
    private var selectedNum = ArrayList<Int>()

    inner class CalenderItemHolder(val binding: ItemCalendarBinding)
        : RecyclerView.ViewHolder(binding.root){
            fun bind(item: DayData){
                //텍스트 표시
                binding.calendarText.text = dataSet[absoluteAdapterPosition].day.toString()
                //각 아이템의 높이 지정
                val params  = LinearLayout.LayoutParams(calendarLayout.width / 7,calendarLayout.height / 6)
                binding.root.layoutParams = params

                //오늘 날짜
                if (dataSet[absoluteAdapterPosition].isSelected){
                    binding.calendarText.apply {
                        setTypeface(this.typeface, Typeface.BOLD_ITALIC)
                    }
                }
                //다른 달 회색
                if(dataSet[absoluteAdapterPosition].monthIndex!=0){
                    binding.calendarText.apply {
                        setTextAppearance(R.style.grayColorText)
                    }
                }
                //클릭 이벤트
                if(onItemClickListener!=null){
                    binding.root.setOnClickListener {
                        onItemClickListener?.onItemClick(item, position = absoluteAdapterPosition)

                        if(selectedNum.size==0){
                            //first click
                            binding.setOneSelected()
                            selectedNum.add(absoluteAdapterPosition)
                        } else if (selectedNum.size==1){
                            //second Click
                            binding.setTwoSelected()
                            selectedNum.add(absoluteAdapterPosition)
                        }else{
                            binding.reset()
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
        holder.bind(dataSet[position])
    }

    override fun getItemCount(): Int = dataSet.size

    private fun ItemCalendarBinding.setOneSelected(){
        itemLine.visibility = View.VISIBLE
        itemLine.setBackgroundResource(R.drawable.task_line_circle)
    }

    private fun ItemCalendarBinding.setTwoSelected(){
        itemLine.visibility = View.VISIBLE
        itemLine.setBackgroundResource(R.drawable.task_line_end)
    }

    private fun ItemCalendarBinding.reset(){
        selectedNum.clear()
    }
    private fun ItemCalendarBinding.setMid(){
        itemLineMid.visibility = View.VISIBLE
        itemLineMid.setBackgroundResource(R.drawable.task_line_mid)
    }

}