package com.example.SwallowMonthJM

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.startActivity
import org.mozilla.javascript.tools.jsc.Main

// 응답을 받는 역할
// 알람 매니저를 통해 트리거 시간과 반복 시간, 생성한 브로드캐스트 리시버를 세팅

class AlarmBroadCastReceiver : BroadcastReceiver() {

    override fun onReceive(p0: Context?, p1: Intent?) {
        // 알람을 받는 곳
        Toast.makeText(p0,"init current Data", Toast.LENGTH_SHORT).show()
        Log.d("init current Data.!","!")
        if (p0!=null && p1!=null && p1.extras?.get("code")==MainActivity.REQUEST_CODE_1) {
            val code =MainActivity.REQUEST_CODE_1
            // 메인으로 이동하는 인텐트 생성
            val intent = Intent(p0,MainActivity::class.java).apply {
                putExtra("username",p1.extras?.get("username").toString())
            }
            // 늦은 인텐트로 전환
            val pendingIntent = PendingIntent.getActivity(p0,0,intent,0)
            // 알림 생성
            val builder = NotificationCompat.Builder(p0, "my_channel")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("[Swallow Notification]")
                .setContentText("update data today!")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
            // 알림 실행
            NotificationManagerCompat.from(p0).apply {
                notify(code,builder.build())
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel("my_channel","Notification",
                    NotificationManager.IMPORTANCE_DEFAULT).apply {
                        description = "Notification"
                }
                val notificationManager = p0.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }

        }
    }

}

