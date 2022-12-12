package com.example.SwallowMonthJM.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.SwallowMonthJM.Calendar.CustomCalendar
import com.example.SwallowMonthJM.Unit.DayData
import com.example.SwallowMonthJM.Unit.Task
import java.text.SimpleDateFormat
import java.util.*

//일정 리스트 관리
class MainViewModel : ViewModel(){
    var todayMonth =0
    var totalPer = 0
    var taskLiveData = MutableLiveData<ArrayList<DayData>>()
    lateinit var currentDate : CustomCalendar
    lateinit var currentMonthArr : ArrayList<DayData>
    lateinit var dateTime:String

    var currentYear = MutableLiveData<Int>()
    var currentMonth=MutableLiveData<Int>()
    var currentDayPosition= MutableLiveData<Int>()
    // 단순 초기화
    init {
        currentYear.value = 2022
        currentMonth.value = 1
        currentDayPosition.value = 1
    }

    fun addTaskData(startNum:Int,endNum:Int,task: Task){
        for (i in startNum..endNum){
            currentMonthArr[i].apply {
                if (this.taskList == null) {
                    this.taskList = ArrayList()
                }
                this.taskList!!.add(task)
            }
        }

        taskLiveData.value = currentMonthArr

    }
    fun delTaskData(position: Int,task: Task){
        currentMonthArr[position].taskList?.remove(task)
        taskLiveData.value = currentMonthArr
    }
    fun doneTaskData(task:Task){
        task.isDone = !task.isDone
        taskLiveData.value = currentMonthArr
    }
    fun setICon(task:Task,iconIndex:Int){
        task.iconType = iconIndex
        taskLiveData.value = currentMonthArr
    }

    fun setCurrentYear(year:Int){
        currentYear.value = year
    }
    fun setCurrentMonth(month:Int){
        currentMonth.value = month
    }
    fun setCurrentDayPosition(dayPosition:Int){
        currentDayPosition.value = dayPosition
    }

    fun setDateTime(year:Int,month:Int){
        dateTime = "$year.$month"
    }

    fun getDate(year: Int,month :Int): Date{
        return Calendar.getInstance().run {
            set(Calendar.YEAR,year)
            set(Calendar.MONTH,month-1)
            time
        }
    }
    fun initCurrentData(data:Date){
        val dateYear : Int = SimpleDateFormat("yyyy",Locale.KOREA).format(data).toInt()
        val dateDay: Int = SimpleDateFormat("dd", Locale.KOREA).format(data).toInt()
        val dateMonth: Int = SimpleDateFormat("MM", Locale.KOREA).format(data).toInt()
        setCurrentYear(dateYear)
        setCurrentMonth(dateMonth)
        setDateTime(dateYear,dateMonth)
        currentDate = CustomCalendar(data,dateDay,todayMonth,dateMonth,dateTime)
        currentDate.initBaseCalendar()
        currentMonthArr = currentDate.dateList
    }
}