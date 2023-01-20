package com.example.SwallowMonthJM.Model

import java.io.Serializable

class Task(
    var id :Int?,
    var monthId: Int,
    var userId : Int,
    var dayIndex : Int,
    var text: String,
    var isDone : Boolean,
    var iconType :Int=0,
    var level : Int,
    var per : Int,
):Serializable