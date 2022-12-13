package com.example.SwallowMonthJM.ViewModel

import androidx.lifecycle.ViewModel
import com.example.SwallowMonthJM.Unit.Task

class AddTaskViewModel : ViewModel(){
    var iconType :Int=-1
    var level :Int = -1
    var text : String = ""
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
    fun reset(){
        iconType =-1
        level= -1
        text  = ""
        startNum = -1
        endNum = -1
    }
}