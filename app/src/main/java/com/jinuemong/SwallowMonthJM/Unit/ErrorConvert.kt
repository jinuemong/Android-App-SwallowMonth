package com.jinuemong.SwallowMonthJM.Unit

import okhttp3.ResponseBody

fun errorConvert(responseBody: ResponseBody?) : String{

    val err  = responseBody?.string()
    return if (err==null){
        "error"
    }else{
        return err
    }
}