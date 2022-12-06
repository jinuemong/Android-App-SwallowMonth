package com.example.SwallowMonthJM.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.SwallowMonthJM.Unit.Task

class AddTaskViewModel : ViewModel(){
    var iconType :Int=-1
    var level :Int = -1
    var text : String = ""
    // 범위
    var startNum = -1
    var endNum = -1

    fun getTaskData():Task?{
        Log.d("dddd_iconType",iconType.toString())
        Log.d("dddd_level",level.toString())
        Log.d("dddd_text",text.toString())
        Log.d("dddd_startNum",startNum.toString())
        Log.d("dddd_endNum",endNum.toString())

        if(iconType==-1){
            return null
        }else if (level==-1){
            return null
        }else if (text==""){
            return null
        }else if (startNum==-1 && endNum==-1) {
            return null
        }else{
            return Task(null,text,false,iconType,level)
        }
    }
}