package com.example.SwallowMonthJM.Model

import java.io.Serializable

class Message (
    val messageId : Int,
    val frId : Int,
    val userId : String,
    val text : String,
    val createTime : String,
): Serializable