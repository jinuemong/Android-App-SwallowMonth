package com.example.SwallowMonthJM.Unit

import java.io.Serializable

class CustomDayData(
    var day :Int,
    var isTodoData :Boolean,
    var inRepeatData : Boolean,
    var isToday : Boolean,
    var isOtherMonth : Boolean
):Serializable