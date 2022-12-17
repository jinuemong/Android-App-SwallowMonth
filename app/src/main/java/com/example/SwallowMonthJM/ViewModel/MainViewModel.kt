package com.example.SwallowMonthJM.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.SwallowMonthJM.Calendar.CustomCalendar
import com.example.SwallowMonthJM.Unit.DayData
import com.example.SwallowMonthJM.Unit.SampleClass
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
            val newTask = SampleClass(task).deepCopy()
            currentMonthArr[i].apply {
                if (this.taskList == null) {
                    this.taskList = ArrayList()
                }
                this.taskList!!.add(newTask.task)
            }

        }
        taskLiveData.value = currentMonthArr

    }

    fun delTaskData(task: Task){
        currentMonthArr[currentDayPosition.value!!].taskList?.remove(task)
        taskLiveData.value = currentMonthArr
    }

    fun doneTaskData(task : Task){
        task.isDone = true
        task.per = 100
        taskLiveData.value = currentMonthArr
    }

    fun setTaskICon(iconIndex:Int,task:Task){
        task.iconType = iconIndex
        taskLiveData.value = currentMonthArr
    }

    fun setPerTask(task:Task,p:Int){
        task.per = p
        taskLiveData.value = currentMonthArr
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
            "0 / 0"
        }else {
            val totalCount = taskList.size
            var taskCount = 0
            for (t in taskList){
                if (!t.isDone){
                    taskCount+=1
                }
            }
            "$taskCount / $totalCount"
        }
    }

}