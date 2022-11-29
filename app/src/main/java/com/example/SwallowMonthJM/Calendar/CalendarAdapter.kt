package com.example.SwallowMonthJM.Calendar

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.SwallowMonthJM.R
import com.example.SwallowMonthJM.Unit.DayData
import com.example.SwallowMonthJM.databinding.ItemCalendarBinding
import java.text.SimpleDateFormat
import java.util.*

//각 캘린더의 어댑터 (날짜 - 일모음 )
class CalendarAdapter(
    private val calendarLayout: LinearLayout,
    private val dateTime: String,
    date: Date,
    currentMonth: Int,
    val viewSlide:(position:Int,keyData:String)->Unit
) : RecyclerView.Adapter<CalendarAdapter.CalenderItemHolder>() {

    //오늘 데이터 얻어옴
    private val dateDay: Int = SimpleDateFormat("dd", Locale.KOREA).format(date).toInt()
    private val dateMonth: Int = SimpleDateFormat("MM", Locale.KOREA).format(date).toInt()
    private var dataSet: ArrayList<DayData> = arrayListOf()
    var customCalendar: CustomCalendar = CustomCalendar(date,dateDay,currentMonth,dateMonth)
    init {
        customCalendar.initBaseCalendar()
        dataSet = customCalendar.dateList
    }

    private val firstDateIndex = customCalendar.prevTail
    private val lastDateIndex = dataSet.size - customCalendar.nextHead - 1

    class CalenderItemHolder(val binding: ItemCalendarBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalenderItemHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_calendar, parent, false)
        return CalenderItemHolder(ItemCalendarBinding.bind(view))
    }


    override fun onBindViewHolder(holder: CalenderItemHolder, position: Int) {

        //텍스트 표시
        holder.binding.calendarText.text = dataSet[position].day.toString()
        //각 아이템의 높이 지정
        holder.binding.root.layoutParams.height = calendarLayout.height / 6

        holder.binding.root.setOnClickListener {
            viewSlide(position,makeKeyData(position))
        }


//        //오늘 날짜 + 현재 달이 오늘 : 진하게
//        if (dataSet[position].isToday) {
//            holder.binding.calendarText.apply {
//                setTypeface(this.typeface, Typeface.BOLD_ITALIC)
//            }
//        }
//
//        //다른 달 : 회색으로 표시
//        if (dataSet[position].isOtherMonth) {
//            holder.binding.calendarText.apply {
//                setTextAppearance(R.style.grayColorText)
//            }
//
//        }
        // 나중에 해결하기 !
//
//        //일정 존재
//        if (dataSet[position].isTodoData){
//            holder.binding.isItTodo.visibility = View.VISIBLE
//        }else{
//            holder.binding.isItTodo.visibility = View.INVISIBLE
//        }


    }

    override fun getItemCount(): Int = dataSet.size

    private fun makeKeyData(position: Int) : String{

        return if (position<firstDateIndex){
            val month = dateTime.replace("[^\\d]".toRegex(),"").substring(4)
            dateTime.replace(month,(month.toInt()-1).toString()) + " " + dataSet[position].day+"일"
        } else if(position>lastDateIndex){
            val month = dateTime.replace("[^\\d]".toRegex(),"").substring(4)
            dateTime.replace(month,(month.toInt()+1).toString()) + " " + dataSet[position].day+"일"
        }else{
            dateTime + " " + dataSet[position].day + "일"
        }

    }

    fun dataReset(changeType:Int,position:Int){
        //다시 해주야 햄!
//        if (changeType==1){
//            dataSet[position].isTodoData = true
//        }else if (changeType==2){
//            dataSet[position].isTodoData = false
//        }
        notifyItemChanged(position)
    }

}