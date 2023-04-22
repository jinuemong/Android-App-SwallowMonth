package com.jinuemong.SwallowMonthJM.Unit

import java.util.*

//Work manager의 시간 설정
// 해당 함수의 리턴값 만큼 대기한 이후부터 시작
// 현재 날짜와 목표 날짜를 계산, 그 차이만큼 대기하여 worker 호출
fun getTimeUsingInWorkRequest() : Long {
    val currentDate = Calendar.getInstance()
    val dueDate = Calendar.getInstance()
        .apply {
            set(Calendar.HOUR_OF_DAY,0)
            set(Calendar.MINUTE,0)
            set(Calendar.SECOND,0)

        }
    if (dueDate.before(currentDate) || dueDate.timeInMillis==currentDate.timeInMillis){
        dueDate.add(Calendar.HOUR_OF_DAY,24)
    }

    return dueDate.timeInMillis - currentDate.timeInMillis
}