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
    var currentMonth=0
    var currentDayPosition=0
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
//    fun addTodoData(key:String,text:String,level: Int){
//
//        currentMonthArr = if (taskData[key] !=null){ taskData[key]!! } else{ ArrayList() }
//
//        currentMonthArr.add(Task(null,text,false,0,level))
//        taskData[key] = currentMonthArr
//
//        taskLiveData.value = taskData
//
//    }
//
//    fun delTodoData(key:String,todo:Task){
//        taskData[key]?.remove(todo)
//        taskLiveData.value = taskData
//    }
//
//    fun doneData(todo:Task){
//        todo.isDone = !todo.isDone
//        taskLiveData.value = taskData
//    }
//
//    fun setIcon(todo:Task, index:Int){
//        todo.iconType = index
//        taskLiveData.value = taskData
//    }
//
//    //서버의 기존 데이터를 불러옴
//    fun roadAllData(){
//
//    }
//
//    fun getKeyData(monthIndex:Int,day:String) : String{
//        dateTime.replace(currentMonth.toString(),(currentMonth+monthIndex).toString())
//        return dateTime +" "+day+"일"
//    }
}