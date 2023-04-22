package com.jinuemong.SwallowMonthJM.Model

class Alarm(
    val alarmId : Int,
    val userId : String,
    val type : String,
    val typeId : Int,
    val fromUserId : String,
    val isRead : Boolean,
    val createTime : String,
): java.io.Serializable