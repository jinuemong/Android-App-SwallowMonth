package com.example.SwallowMonthJM.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.SwallowMonthJM.Model.Routine
import com.example.SwallowMonthJM.Model.Task

class AddTaskRoutineViewModel : ViewModel(){
    var addType = "task"
    var iconType :Int=-1
    var level :Int = -1
    var text : String = ""
    var cycle:Int = 99
    var keyData : String = ""
    var totalRoutine : Int = -1
    var topText :String = ""
    // 범위
    var startNum = -1
    var endNum = -1
    var routineChange = MutableLiveData<Boolean>(false)

    fun getTaskData(): Task? {

        return if (iconType == -1) {
            null
        } else if (level == -1) {
            null
        } else if (text == "") {
            null
        } else if (startNum == -1 && endNum == -1) {
            null
        } else {
            Task(null,null, text, false, iconType, level+1,0)
        }
    }
    fun getTextData():String{
        return text
    }

    fun getRoutineData(): Routine?{

        return if (iconType== -1){
            null
        } else if (cycle== 99){
            null
        } else if (text==""){
            null
        } else if (totalRoutine==-1) {
            null
        } else if (topText=="") {
            null
        } else if (keyData==""){
            null
        }else{
            Routine(null,keyData,text,cycle,startNum,totalRoutine,
                0,iconType,topText,HashMap())
        }
    }
    fun reset(){
        iconType =-1
        level= -1
        text  = ""
        startNum = -1
        keyData = ""
        endNum = -1
        cycle = 99
        totalRoutine = -1
        topText = ""
    }
}