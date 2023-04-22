package com.jinuemong.SwallowMonthJM.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jinuemong.SwallowMonthJM.Model.Routine
import com.jinuemong.SwallowMonthJM.Model.Task

class AddTaskRoutineViewModel : ViewModel(){
    var monthId = -1
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
        } else if (keyData==""){
            null
        } else {
            Task(null,-1,"-1", -1,text, false, iconType, level+1,0)
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
        Routine(null,"-1",-1,keyData,text,cycle,startNum,totalRoutine,
                0,iconType,topText,ArrayList())
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
        monthId = -1
    }
}