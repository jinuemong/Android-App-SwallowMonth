package com.example.SwallowMonthJM.Unit

import okhttp3.ResponseBody

fun errorConvert(responseBody: ResponseBody?) : String{

    val err  = responseBody?.string()
    return if (err==null){
        "error"
    }else{
       val errList = err.split(":")
        return "{$errList[2]}"
    }
}