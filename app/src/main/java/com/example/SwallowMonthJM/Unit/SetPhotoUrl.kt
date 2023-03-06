package com.example.SwallowMonthJM.Unit

fun getPhotoUrl(url: String,baseUrl:String):String{
    return if (checkPhotoUrl(url)){
        url // 그대로 반환
    }else{
        baseUrl+url
    }
}

private fun checkPhotoUrl(url: String) : Boolean{
    return url.contains("https")
}