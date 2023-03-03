package com.example.SwallowMonthJM.Model

class Alarm(
    val alarmId : Int,
    val userId : String,
    val type : String,
    val typeId : Int,
    val isRead : Boolean
): java.io.Serializable