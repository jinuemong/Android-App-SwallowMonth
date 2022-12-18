package com.example.SwallowMonthJM.Unit

import java.io.Serializable

class Routine (
    var id : Int?,
    var text : String?,
    var period : Int,
    var totalRoutine : Int,
    var clearRoutine : Int,
    var iconType:Int = 0,
):Serializable