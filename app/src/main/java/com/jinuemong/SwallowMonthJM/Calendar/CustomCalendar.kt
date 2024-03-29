package com.jinuemong.SwallowMonthJM.Calendar

import com.jinuemong.SwallowMonthJM.MainActivity
import com.jinuemong.SwallowMonthJM.Model.DayData
import java.text.SimpleDateFormat
import java.util.*

//각 달의 일수 표현
class CustomCalendar(
    private val mainActivity: MainActivity,
    date: Date,
    val currentDay:Int,
    private val currentMonth:Int,
    private val dateMonth:Int,

) {
    //주당 7일 * 6주 총 42개의 데이터 전달
    companion object {
        const val DAYS_OF_WEEK= 7
        const val LOW_OF_CALENDAR =6
    }
    var currentIndex = 0
    val calendar: Calendar = Calendar.getInstance()
    var prevTail = 0 //이전달 끝부분
    var nextHead = 0 //다음달 앞부분
    var currentMaxDate = 0 //현재 달
    var dateList = ArrayList<DayData>()
    var keyDate:String = ""
    var topDate : String = ""

    init {
        calendar.time=date
        keyDate  = SimpleDateFormat("yyyy.MM", Locale.KOREA).format(date)
        topDate = SimpleDateFormat("yyyy년 MM월", Locale.KOREA).format(date)
    }

    //해당 fragment 에서 실행 해줌
    fun initBaseCalendar(){
        makeMonthDate()
    }

    private fun makeMonthDate(){
        dateList.clear()
        //현재 일 세팅
        calendar.set(Calendar.DATE,1)
        //해당 달의 마지막 일 구함
        currentMaxDate = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        //이전 달 세팅
        prevTail = calendar.get(Calendar.DAY_OF_WEEK) -1

        //전 달 , 이번 달, 다음 달 순으로 총 6주(=42)의 달력을 만듦
        makePrevTail(calendar.clone() as Calendar)

        makeCurrentMonth(calendar)

        //전체 일 수에서 이전의 마지막 + 이번달의 최종 일을 더한 값을 빼줌 = 다음달의  표시할 일 수
        nextHead = LOW_OF_CALENDAR* DAYS_OF_WEEK - (prevTail + currentMaxDate)
        makeNextHead()
    }

    private fun makePrevTail(calendar: Calendar){
        val monthIndex = -1
        calendar.set(Calendar.MONTH,calendar.get(Calendar.MONTH)-1)
        val maxDate = calendar.getActualMaximum(Calendar.DATE)
        var maxOffsetDate = maxDate-prevTail
        for (i in 1..prevTail){
            dateList.add(
                DayData(
                    getCurrentData(monthIndex),
                    ++maxOffsetDate,
                    isSelected = false,
                    monthIndex = monthIndex,
                )
            )
        }
    }

    private fun makeCurrentMonth(calendar: Calendar){
        for (i in 1..calendar.getActualMaximum(Calendar.DATE)) {
            val isToday = (currentDay==i && currentMonth==dateMonth)
            dateList.add(
                DayData(
                    keyDate,
                    i,
                    isSelected = isToday,
                    monthIndex = 0,
                )
            )
            if(isToday){
                currentIndex = dateList.lastIndex
            }
        }
    }

    private fun makeNextHead(){
        val monthIndex = 1
        var date = 1
        for (i in 1..nextHead){
            dateList.add(
                DayData(
                    getCurrentData(monthIndex),
                    date++,
                    isSelected = false,
                    monthIndex = monthIndex,
                )
            )

        }
    }

    private fun getCurrentData(monthIndex : Int) : String{
        val date = keyDate.split(".")
        val year = date[0].toInt()

        return when(val month = date[1].toInt()+monthIndex){

            0 ->{ "${year-1}.${12}" }

            13 ->{ "${year+1}.${1}" }

            else ->{ "$year.${month}" }
        }
    }
}