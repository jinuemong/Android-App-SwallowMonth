package com.example.SwallowMonthJM.Unit

import java.io.Serializable

class Todo(
    var id :Int?,
    var text: String,
    var isDone : Boolean,
    var iconType :Int=0,
):Serializable