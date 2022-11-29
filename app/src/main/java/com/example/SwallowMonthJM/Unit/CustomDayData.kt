package com.example.SwallowMonthJM.Unit

import java.io.Serializable

class CustomDayData(
    var day :Int,
    var isTodoData :Boolean,
    var isToday : Boolean,
    var routineList : ArrayList<Routine>?,
    var todoList : ArrayList<Todo>?
):Serializable