package com.example.SwallowMonthJM.Model

import java.io.Serializable

class Routine (
    var routineId : Int,
    var userId : String,
    var monthId : Int,
    var keyDate : String,
    var text : String,
    var cycle : Int,
    var startNum : Int,
    var totalRoutine : Int,
    var clearRoutine : Int,
    var iconType:Int = 0,
    var topText : String,
    var dayRoutinePost : ArrayList<DayRoutine>,
):Serializable