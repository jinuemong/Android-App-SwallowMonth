package com.example.SwallowMonthJM.Unit

import java.io.Serializable

class DayData(
    var day :Int,
    var isToday : Boolean,
    var monthIndex : Int,
    var routineList : ArrayList<Routine>?,
    var todoList : ArrayList<Todo>?
):Serializable