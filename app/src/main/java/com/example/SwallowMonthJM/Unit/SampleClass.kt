package com.example.SwallowMonthJM.Unit

import com.google.gson.Gson

// 깊은 복사를 위한 클래스 선언
data class SampleClass(var task:Task){
    fun deepCopy() : SampleClass{
        return Gson().fromJson(Gson().toJson(this),this::class.java)
    }
}