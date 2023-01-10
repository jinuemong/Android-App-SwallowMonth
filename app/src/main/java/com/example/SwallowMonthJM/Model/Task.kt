package com.example.SwallowMonthJM.Model

import java.io.Serializable

class Task(
    var id :Int?,
    var dayDataId: Int?,
    var text: String,
    var isDone : Boolean,
    var iconType :Int=0,
    var level : Int,
    var per : Int,
):Serializable