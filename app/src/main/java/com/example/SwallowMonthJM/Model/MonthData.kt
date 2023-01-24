package com.example.SwallowMonthJM.Model

import java.io.Serializable

class MonthData (
    var monthId : Int?,
    var userId : String,
    var keyDate : String,
    var totalPer : Int,
    var totalPoint : Int,
    var taskCount : Int,
    var dayRoutineCount : Int,
    var doneTask : Int,
    var clearRoutine : Int,
):Serializable