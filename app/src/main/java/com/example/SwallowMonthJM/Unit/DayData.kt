package com.example.SwallowMonthJM.Unit

import java.io.Serializable

class DayData(
    var keyDate : String,
    var day :Int,
    var isSelected : Boolean,
    var monthIndex : Int,
    var routineList : ArrayList<Routine>?,
    var taskList : ArrayList<Task>?
):Serializable