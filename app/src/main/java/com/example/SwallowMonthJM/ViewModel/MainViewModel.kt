package com.example.SwallowMonthJM.ViewModel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.SwallowMonthJM.Calendar.CustomCalendar
import com.example.SwallowMonthJM.MainActivity
import com.example.SwallowMonthJM.Manager.MonthDataManager
import com.example.SwallowMonthJM.Manager.RoutineManager
import com.example.SwallowMonthJM.Manager.TaskManager
import com.example.SwallowMonthJM.Model.DayData
import com.example.SwallowMonthJM.Model.MonthData
import com.example.SwallowMonthJM.Model.Profile
import com.example.SwallowMonthJM.Server.MasterApplication
import java.text.SimpleDateFormat
import java.util.*

//일정 리스트 관리
class MainViewModel : ViewModel(){

    lateinit var profile :Profile
    lateinit var monthDataManager:MonthDataManager

    //오늘 데이터 저장 ///////////////////
    lateinit var todayDate : Date
    var todayYear = 0
    var todayMonth =0
    var todayDayPosition = 0
    //////////////////////////////////

    //현재 view 데이터  /////////////////////
    lateinit var monthData:MonthData
    var dayLiveData = MutableLiveData<ArrayList<DayData>>()
    lateinit var currentDate : CustomCalendar
    var currentMonthArr = ArrayList<DayData>()
    var currentYear = MutableLiveData<Int>()
    var currentMonth=MutableLiveData<Int>()
    var currentDayPosition= MutableLiveData<Int>()
    //////////////////////////////////////////

    //관찰 데이터//////////////////////////
    private val _event_statistics = SingleLiveEvent<Any>()
    private val _event_taskList = SingleLiveEvent<Any>()
    val eventStatistics : LiveData<Any> get() = _event_statistics
    val eventTaskList : LiveData<Any> get() = _event_taskList

    private fun setStatistics(){
        _event_statistics.call()
    }
    private fun setTaskList(){
        _event_taskList.call()
    }
    /////////////////////////////////////
    // 단순 초기화
    init {
        dayLiveData.value = currentMonthArr
        currentYear.value = 2022
        currentMonth.value = 1
        currentDayPosition.value = 1
    }

    //현재 뷰 년도 초기화
    private fun setCurrentYear(year:Int){
        currentYear.value = year
    }

    //현재 뷰 달 초기화
    private fun setCurrentMonth(month:Int){
        currentMonth.value = month
    }

    //현재 뷰 day position 초기화
    fun setCurrentDayPosition(dayPosition:Int){
        currentDayPosition.value = dayPosition
    }

    //현재 뷰 Date 구함
    fun getDate(year: Int,month :Int): Date{
        return Calendar.getInstance().run {
            set(Calendar.YEAR,year)
            set(Calendar.MONTH, month - 1)
            //2월은 윤년이 아니면 3월로 침 ->1월로 초기화
            set(Calendar.DAY_OF_MONTH,1)
            time
        }
    }

    //현재 뷰 갱신
    fun setCurrentData(data:Date, mainActivity: MainActivity){
        val dateYear : Int = SimpleDateFormat("yyyy",Locale.KOREA).format(data).toInt()
        val dateDay: Int = SimpleDateFormat("dd", Locale.KOREA).format(data).toInt()
        val dateMonth: Int = SimpleDateFormat("MM", Locale.KOREA).format(data).toInt()

        setCurrentYear(dateYear)
        setCurrentMonth(dateMonth)

        val keyDate = SimpleDateFormat("yyyy.MM", Locale.KOREA).format(data)
        monthData = MonthData(null,profile.userName,keyDate,
            0,0,0,
            0,0,0)
        monthDataManager.getKeyDateMonthData(profile.userName,keyDate, paramFun = { mData ->
            if(mData==null){
                Toast.makeText(mainActivity,"Network error", Toast.LENGTH_SHORT)
                    .show()
            }else{
                if (mData.size>0){ //기존 데이터가 있을 경우
                    monthData = mData[0]
                    //routine 데이터 설정
                    val routineManager = RoutineManager(mainActivity.application as MasterApplication)
                    routineManager.getRoutineList(mainActivity.userName,monthData.keyDate, paramFun = {
                        if(it!=null) {
                            mainActivity.routineViewModel.currentRoutineArr = it
                            for (i in mainActivity.routineViewModel.currentRoutineArr){
                                Log.d("!!!routine data: ",i.toString())
                            }
                        }else{
                            mainActivity.routineViewModel.currentRoutineArr = ArrayList()
                        }
                        mainActivity.routineViewModel.routineLivData.value = mainActivity.routineViewModel.currentRoutineArr

                    })
                    //task 데이터 설정
                    val taskManager = TaskManager(mainActivity.application as MasterApplication)
                    taskManager.getTaskList(mainActivity.userName, monthData.monthId!!, paramFun = {
                        if (it != null) {
                            mainActivity.taskViewModel.currentTaskArr = it
                            for (i in mainActivity.taskViewModel.currentTaskArr) {
                                Log.d("!!!task data: ", i.toString())
                            }
                        } else {
                            mainActivity.taskViewModel.currentTaskArr = ArrayList()
                        }
                        mainActivity.taskViewModel.taskLiveData.value = mainActivity.taskViewModel.currentTaskArr
                    })
                }
            }
        })
        //현재 view 데이터 생성
        currentDate = CustomCalendar(mainActivity,data, dateDay, todayMonth, dateMonth)
        currentDate.initBaseCalendar()
        currentMonthArr = currentDate.dateList
        dayLiveData.value = currentMonthArr

        //오늘 날짜 초기화
        if(todayYear==0) {
            todayDate = data
            todayYear =dateYear
            todayDayPosition = currentDate.currentIndex
            currentDayPosition.value = currentDate.currentIndex
        }
        setCurrentDayPosition(currentDayPosition.value!!)

        //데이터 변화 알림
        setStatistics()
        setTaskList()
    }

    //남은 task + routine 설정
    fun getActivityList() : String{
        val totalList = monthData.taskCount+monthData.dayRoutineCount
        val clearList = monthData.doneTask+monthData.clearRoutine
        return "${totalList-clearList}/$totalList"
    }


}