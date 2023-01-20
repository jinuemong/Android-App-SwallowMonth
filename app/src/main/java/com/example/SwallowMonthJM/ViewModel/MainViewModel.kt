package com.example.SwallowMonthJM.ViewModel

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.SwallowMonthJM.Calendar.CustomCalendar
import com.example.SwallowMonthJM.MainActivity
import com.example.SwallowMonthJM.Manager.MonthDataManager
import com.example.SwallowMonthJM.Model.DayData
import com.example.SwallowMonthJM.Model.MonthData
import com.example.SwallowMonthJM.Model.Profile
import java.text.SimpleDateFormat
import java.util.*

//일정 리스트 관리
class MainViewModel : ViewModel(){
    lateinit var profile : Profile
    lateinit var monthDataManager:MonthDataManager

    //오늘 데이터 저장 ///////////////////
    lateinit var todayDate : Date
    var todayYear = 0
    var todayMonth =0
    var todayDayPosition = 0
    //////////////////////////////////

    //현재 view 데이터  /////////////////////
    lateinit var monthData:MonthData
//    var keyDate : String,
//    var totalPer : Int,
//    var totalPoint : Int,
//    var taskCount : Int,
//    var dayRoutineCount : Int,
//    var doneTask : Int,
//    var clearRoutine : Int,

    var dayLiveData = MutableLiveData<ArrayList<DayData>>()
    lateinit var currentDate : CustomCalendar
    var currentMonthArr = ArrayList<DayData>()

    var currentYear = MutableLiveData<Int>()
    var currentMonth=MutableLiveData<Int>()
    var currentDayPosition= MutableLiveData<Int>()
    //////////////////////////////////////////

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
    fun initCurrentData(data:Date,mainActivity: MainActivity){
        todayDate = data
        val dateYear : Int = SimpleDateFormat("yyyy",Locale.KOREA).format(data).toInt()
        val dateDay: Int = SimpleDateFormat("dd", Locale.KOREA).format(data).toInt()
        val dateMonth: Int = SimpleDateFormat("MM", Locale.KOREA).format(data).toInt()
        setCurrentYear(dateYear)
        setCurrentMonth(dateMonth)
        val keyDate = SimpleDateFormat("yyyy.MM", Locale.KOREA).format(data)

        monthDataManager.getKeyDateMonthData(profile.userName,keyDate, paramFun = {
            if(it==null){
                Toast.makeText(mainActivity,"Network error", Toast.LENGTH_SHORT)
                    .show()
            }else{
                if (it.size>0){ //기존 데이터가 있을 경우
                    monthData = it[0]
                }else{ //기존 데이터가 없을 경우 새로 생성
                    monthData = MonthData(null,profile.userName,keyDate,
                        0,0,0,
                        0,0,0)
                }
            }
        })
        //현재 view 데이터 생성
        currentDate = CustomCalendar(data, keyDate, dateDay, todayMonth, dateMonth)
        currentDate.initBaseCalendar()
        currentMonthArr = currentDate.dateList

        if(todayYear==0) {
            todayYear =dateYear
            todayDayPosition = currentDate.currentIndex
        }
    }

    //남은 task + routine 설정
    fun getActivityList() : String{
        var totalList = monthData.taskCount+monthData.dayRoutineCount
        var clearList = monthData.doneTask+monthData.clearRoutine
        return "${totalList-clearList}/$totalList"
    }


}