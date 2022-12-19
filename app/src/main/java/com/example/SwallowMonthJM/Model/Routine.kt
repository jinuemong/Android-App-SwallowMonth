package com.example.SwallowMonthJM.Model

import java.io.Serializable

class Routine (
    var id : Int?,
    var text : String?,
    var cycle : Int,
    var startNum : Int,
    var totalRoutine : Int,
    var clearRoutine : Int,
    var iconType:Int = 0,
):Serializable