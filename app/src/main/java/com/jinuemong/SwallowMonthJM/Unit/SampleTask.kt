package com.jinuemong.SwallowMonthJM.Unit

import com.jinuemong.SwallowMonthJM.Model.Routine
import com.jinuemong.SwallowMonthJM.Model.Task
import com.google.gson.Gson

// 깊은 복사를 위한 클래스 선언
data class SampleTask(var task: Task){
    fun deepCopy() : SampleTask{
        return Gson().fromJson(Gson().toJson(this),this::class.java)
    }
}

data class SampleRoutine(var routine: Routine){
    fun deepCopy() : SampleRoutine{
        return Gson().fromJson(Gson().toJson(this),this::class.java)
    }
}