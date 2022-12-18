package com.example.SwallowMonthJM.ViewModel

import androidx.lifecycle.ViewModel
import com.example.SwallowMonthJM.Unit.Routine
import com.example.SwallowMonthJM.Unit.Task

class AddTaskViewModel : ViewModel(){
    var addType = "task"
    var iconType :Int=-1
    var level :Int = -1
    var text : String = ""
    var period:Int = -1
    var totalRoutine : Int = -1
    // 범위
    var startNum = -1
    var endNum = -1

    fun getTaskData():Task? {

        return if (iconType == -1) {
            null
        } else if (level == -1) {
            null
        } else if (text == "") {
            null
        } else if (startNum == -1 && endNum == -1) {
            null
        } else {
            Task(null, text, false, iconType, level+1,0)
        }
    }
    fun getTextData():String{
        return text
    }

    fun getRoutineData():Routine?{

        return if (iconType== -1){
            null
        } else if (period== -1){
            null
        } else if (text==""){
            null
        } else if (totalRoutine==-1){
            null
        }else{
            Routine(null,text,period,totalRoutine,0,iconType)
        }
    }
    fun reset(){
        iconType =-1
        level= -1
        text  = ""
        startNum = -1
        endNum = -1
    }
}