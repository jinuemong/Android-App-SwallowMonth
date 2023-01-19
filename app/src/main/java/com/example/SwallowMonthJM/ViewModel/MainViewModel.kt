package com.example.SwallowMonthJM.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.SwallowMonthJM.Calendar.CustomCalendar
import com.example.SwallowMonthJM.Manager.DayDateManager
import com.example.SwallowMonthJM.Model.DayData
import com.example.SwallowMonthJM.Model.Profile
import java.text.SimpleDateFormat
import java.util.*

//일정 리스트 관리
class MainViewModel : ViewModel(){
    lateinit var profile : Profile
    lateinit var dayDataManager:DayDateManager
    lateinit var todayDate : Date
    var todayYear = 0
    var todayMonth =0
    var todayDayPosition = 0

    var totalPer = 0
    var totalPoint = 0
    var dayLiveData = MutableLiveData<ArrayList<DayData>>()

    var currentTotalTask = 0
    var currentTotalRoutine = 0
    var currentTaskCount = 0
    var currentRoutineCount = 0
    lateinit var currentDate : CustomCalendar
    var currentMonthArr = ArrayList<DayData>()

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
        val keyDate = SimpleDateFormat("yyyy.MM", Locale.KOREA).format(data)

        //서버와 연결 - 해당 데이터가 없을 경우 새로 생성
        dayDataManager.getKeyDateDayData(profile.userName,keyDate, paramFun = {
            if (it!=null){
                if (it.size>0){
                    currentMonthArr = it
                }else{
                    //데이터가 없을 경우 새로 생성
                    currentDate = CustomCalendar(data,keyDate,dateDay,todayMonth,dateMonth)
                    currentDate.initBaseCalendar()
                    currentMonthArr = currentDate.dateList
                }
            }
        })

        if(todayYear==0) {
            todayYear =dateYear
            todayDayPosition = currentDate.currentIndex
        }
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