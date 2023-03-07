package com.example.SwallowMonthJM.ViewModel

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
import com.example.SwallowMonthJM.Network.MasterApplication
import java.text.SimpleDateFormat
import java.util.*

//일정 리스트 관리
class MainViewModel : ViewModel(){

    lateinit var myProfile :Profile
    lateinit var monthDataManager:MonthDataManager
    private var _eventSetProfile = SingleLiveEvent<Any>()
    val eventSetData : LiveData<Any> get() = _eventSetProfile
    fun setProfile(newProfile: Profile){
        myProfile = newProfile
        _eventSetProfile.call()
    }
    //오늘 데이터 저장 ///////////////////
    lateinit var todayDate : Date
    lateinit var todayKeyDate:String
    var todayYear = 0
    var todayMonth =0
    var todayDayPosition = 0
    var todayCalPosition = 0 //안 잘린 달력에서 사용
    //////////////////////////////////

    //현재 view 데이터  /////////////////////
    lateinit var monthData:MonthData
    var dayLiveData = MutableLiveData<ArrayList<DayData>>()
    lateinit var currentDate : CustomCalendar
    var currentMonthArr = ArrayList<DayData>()
    var currentYear = MutableLiveData<Int>()
    var currentMonth=MutableLiveData<Int>()
    var currentDayPosition= MutableLiveData<Int>()
    var currentCalPosition = 0
    //////////////////////////////////////////

    //관찰 데이터//////////////////////////
//    private val _eventSetData = SingleLiveEvent<Any>()
//    val eventSetData : LiveData<Any> get() = _eventSetData
//
//    private fun setData(){
//        _eventSetData.call()
//    }
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
        currentCalPosition = dayPosition-currentDate.prevTail
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

        //새로 monthData 생성
        monthData = MonthData(null,myProfile.userName,keyDate,
            0,0,0,
            0,0,0)

        //만약 monthData가 존재하면 불러오기 (존재한다면 Task,Routine도 있음)
        monthDataManager.getKeyDateMonthData(myProfile.userName,keyDate, paramFun = { mData, success ->
            if(!success){ //not true -> network err
                Toast.makeText(mainActivity,"Network error", Toast.LENGTH_SHORT)
                    .show()
            }else{
                val monthId = if (mData!=null && mData.size>0 ){
                    mData[0].monthId
                }else null

                if (monthId!=null){ //기존 데이터가 있을 경우

                    monthData = mData!![0]

                    //routine 데이터 설정
                    val routineManager = RoutineManager(mainActivity.masterApp)
                    routineManager.getRoutineList(mainActivity.userName,monthData.keyDate, paramFun = {
                        if(it!=null) {
                            mainActivity.routineViewModel.currentRoutineArr = it
                        }else{
                            mainActivity.routineViewModel.currentRoutineArr = ArrayList()
                        }
                        mainActivity.routineViewModel.routineLivData.value = mainActivity.routineViewModel.currentRoutineArr
                    })

                    //task 데이터 설정
                    val taskManager = TaskManager(mainActivity.masterApp)
                    taskManager.getTaskList(mainActivity.userName, monthData.monthId!!, paramFun = {
                        if (it != null) {
                            mainActivity.taskViewModel.currentTaskArr = it
                        } else {
                            mainActivity.taskViewModel.currentTaskArr = ArrayList()
                        }
                        mainActivity.taskViewModel.taskLiveData.value = mainActivity.taskViewModel.currentTaskArr
                    })
                }else{
                    mainActivity.taskViewModel.apply {
                        currentTaskArr.clear()
                        taskLiveData.value = currentTaskArr
                    }
                    mainActivity.routineViewModel.apply {
                        currentRoutineArr.clear()
                        routineLivData.value = currentRoutineArr
                    }
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
            todayDayPosition = currentDate.currentIndex //오늘날짜
            todayCalPosition = currentDate.currentIndex-currentDate.prevTail //오늘 달력 인덱스
            todayKeyDate = currentDate.keyDate //오늘 keyDate
        }
        setCurrentDayPosition(currentDate.currentIndex -currentDate.prevTail ) //view 날짜
    }

}