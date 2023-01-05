package com.example.SwallowMonthJM.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.SwallowMonthJM.Calendar.CustomCalendar
import com.example.SwallowMonthJM.Model.DayData
import java.text.SimpleDateFormat
import java.util.*

//일정 리스트 관리
class MainViewModel : ViewModel(){
    var todayMonth =0
    var totalPer = 0
    var totalPoint = 0
    var dayLiveData = MutableLiveData<ArrayList<DayData>>()
    var currentTotalTask = 0
    var currentTotalRoutine = 0
    var currentTaskCount = 0
    var currentRoutineCount = 0
    lateinit var currentDate : CustomCalendar
    var currentMonthArr = ArrayList<DayData>()
    lateinit var dateTime:String
    lateinit var todayDate : Date
    var currentYear = MutableLiveData<Int>()
    var currentMonth=MutableLiveData<Int>()
    var currentDayPosition= MutableLiveData<Int>()

    // 단순 초기화
    init {
        dayLiveData.value = currentMonthArr
        currentYear.value = 2022
        currentMonth.value = 1
        currentDayPosition.value = 1
    }

    private fun setCurrentYear(year:Int){
        currentYear.value = year
    }
    private fun setCurrentMonth(month:Int){
        currentMonth.value = month
    }
    private fun setDateTime(year:Int, month:Int){
        dateTime = "$year.$month"
    }

    fun setCurrentDayPosition(dayPosition:Int){
        currentDayPosition.value = dayPosition
    }


    fun getDate(year: Int,month :Int): Date{
        return Calendar.getInstance().run {
            set(Calendar.YEAR,year)
            set(Calendar.MONTH,month-1)
            time
        }
    }
    fun initCurrentData(data:Date){
        todayDate = data
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

    fun getTotalTask(): String{
        val taskList = currentMonthArr[currentDayPosition.value!!].taskList
        return if (taskList==null){
            "$currentRoutineCount / $currentTotalRoutine"
        }else {
            val taskCount = taskList.count{
                !it.isDone
            }
            currentTaskCount = taskCount
            currentTotalTask = taskList.size
            "${currentTaskCount+currentRoutineCount} / ${currentTotalTask+currentTotalRoutine}"
        }
    }

}