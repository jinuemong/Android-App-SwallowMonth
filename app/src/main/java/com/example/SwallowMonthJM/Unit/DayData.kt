package com.example.SwallowMonthJM.Unit

import java.io.Serializable

class DayData(
    var day :Int,
    var isSelected : Boolean,
    var monthIndex : Int,
    var routineList : ArrayList<Routine>?,
    var todoList : ArrayList<Task>?
):Serializable