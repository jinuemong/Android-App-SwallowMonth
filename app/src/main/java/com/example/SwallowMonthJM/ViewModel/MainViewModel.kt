package com.example.SwallowMonthJM.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.SwallowMonthJM.Calendar.CustomCalendar
import com.example.SwallowMonthJM.Unit.DayData
import com.example.SwallowMonthJM.Unit.Task

//일정 리스트 관리
class MainViewModel : ViewModel(){
    var totalPer = 0
    var taskLiveData = MutableLiveData<ArrayList<DayData>>()
    lateinit var currentDate : CustomCalendar
    lateinit var currentMonthArr : ArrayList<DayData>
    lateinit var dateTime:String

    var currentMonth=MutableLiveData<Int>()
    var currentDayPosition= MutableLiveData<Int>()
    init {
        currentMonth.value = 0
        currentDayPosition.value = 0
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

    fun setCurrentMonth(month:Int){
        currentMonth.value = month
    }
    fun setCurrentDayPosition(dayPosition:Int){
        currentDayPosition.value = dayPosition
    }
}