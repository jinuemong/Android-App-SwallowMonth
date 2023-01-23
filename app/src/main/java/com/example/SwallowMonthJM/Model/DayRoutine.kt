package com.example.SwallowMonthJM.Model

import java.io.Serializable

class DayRoutine(
    var id : Int?,
    var routineId: Int,
    var monthId : Int,
    var dayIndex : Int,
    var clear : Boolean
):Serializable