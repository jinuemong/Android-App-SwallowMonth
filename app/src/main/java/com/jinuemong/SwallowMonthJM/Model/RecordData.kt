package com.jinuemong.SwallowMonthJM.Model

import java.io.Serializable

data class RecordData(
    val recordId : Int,
    val ranking : Int,
    val userId : String,
    val keyDate : String,
    val totalPer : Int,
    val totalPoint : Int,
    val activityNum : Int,
    val clearNum : Int,
): Serializable