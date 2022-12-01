package com.example.SwallowMonthJM.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.SwallowMonthJM.Calendar.CustomCalendar
import com.example.SwallowMonthJM.Unit.Task

//일정 리스트 관리
class MainViewModel : ViewModel(){
    var totalPer = 0
    var recentlyAddData = MutableLiveData<HashMap<String,ArrayList<Task>>>()
    var todoData = HashMap<String, ArrayList<Task>>()
    var currentMonthArr = ArrayList<Task>()

    lateinit var currentDate : CustomCalendar

    lateinit var dateTime:String
    var currentMonth=0

    fun addTodoData(key:String,text:String,level: Int){

        currentMonthArr = if (todoData[key] !=null){ todoData[key]!! } else{ ArrayList() }

        currentMonthArr.add(Task(null,text,false,0,level))
        todoData[key] = currentMonthArr

        recentlyAddData.value = todoData

    }

    fun delTodoData(key:String,todo:Task){
        todoData[key]?.remove(todo)
        recentlyAddData.value = todoData
    }

    fun doneData(todo:Task){
        todo.isDone = !todo.isDone
        recentlyAddData.value = todoData
    }

    fun setIcon(todo:Task, index:Int){
        todo.iconType = index
        recentlyAddData.value = todoData
    }

    //서버의 기존 데이터를 불러옴
    fun roadAllData(){

    }

    fun getKeyData(monthIndex:Int,day:String) : String{
        dateTime.replace(currentMonth.toString(),(currentMonth+monthIndex).toString())
        return dateTime +" "+day+"일"
    }
}