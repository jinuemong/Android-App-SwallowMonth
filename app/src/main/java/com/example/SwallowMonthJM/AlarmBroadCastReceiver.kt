package com.example.SwallowMonthJM

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat.startActivity

// 응답을 받는 역할
// 알람 매니저를 통해 트리거 시간과 반복 시간, 생성한 브로드캐스트 리시버를 세팅

class AlarmBroadCastReceiver : BroadcastReceiver() {

    override fun onReceive(p0: Context?, p1: Intent?) {
        // 알람을 받는 곳
        Log.d("alarm!!!!!",p0.toString()+","+p1.toString())

    }
}