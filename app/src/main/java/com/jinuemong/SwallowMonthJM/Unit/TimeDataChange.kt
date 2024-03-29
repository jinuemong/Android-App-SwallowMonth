package com.jinuemong.SwallowMonthJM.Unit

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
fun getTimeText(createTime: String): String {
    val uploadTimeStamp = createTime.split("-", "T", ":", ".")
    val timeData = uploadTimeStamp[0] + "-" + uploadTimeStamp[1] + "-" + uploadTimeStamp[2] +
            " " + uploadTimeStamp[3] + ":" + uploadTimeStamp[4] + ":" + uploadTimeStamp[5].chunked(2)[0]
    val timeString = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timeData)?.time
    val today = Calendar.getInstance().time.time
    return if (timeString != null) {
        val calDate = (today - timeString)
        if ((calDate / (60 * 60 * 24 * 1000)) >= 1) { //1일 이상
            (calDate / (60 * 60 * 24 * 1000)).toInt().toString() + " day ago"
        } else if ((calDate / (60 * 60 * 1000)) >= 1) { //1시간 이상
            (calDate / (60 * 60 * 1000)).toInt().toString() + " hour ago"
        } else if ((calDate / (60 * 1000)) >= 1) { //분 단위
            (calDate / (60 * 1000)).toInt().toString() + " minute age"
        } else {
            "몇초 전"
        }
    } else { "" }
}