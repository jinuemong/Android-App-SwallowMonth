package com.example.SwallowMonthJM

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.*
import com.example.SwallowMonthJM.MainActivity.Companion.REQUEST_CODE_1
import com.example.SwallowMonthJM.MainActivity.Companion.REQUEST_CODE_2
import com.example.SwallowMonthJM.Unit.getTimeUsingInWorkRequest
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import java.util.Calendar
import java.util.concurrent.TimeUnit

class ScheduledWorker(context: Context, params:WorkerParameters)
    : CoroutineWorker(context,params){

    override suspend fun doWork(): Result = coroutineScope {

            // 실행
            if (Calendar.getInstance().get(Calendar.DAY_OF_MONTH)==1){
                applicationContext.sendBroadcast(monthBroadIntent())
            }else{
                applicationContext.sendBroadcast(dayBroadIntent())
            }
        Result.success()
    }

    //앱 실행 알림
    private fun dayBroadIntent() :Intent {
        return Intent(applicationContext,AlarmBroadCastReceiver::class.java)
            .let {intent->
                intent.putExtra("code", REQUEST_CODE_1)
//                PendingIntent.getBroadcast(applicationContext,REQUEST_CODE_1,intent,PendingIntent.FLAG_IMMUTABLE)
            }
    }

    private fun monthBroadIntent() : Intent{
         return Intent(applicationContext,AlarmBroadCastReceiver::class.java)
            .let { intent ->
                intent.putExtra("code", REQUEST_CODE_2)
//                PendingIntent.getBroadcast(applicationContext,REQUEST_CODE_2,intent,PendingIntent.FLAG_IMMUTABLE)
            }
    }
}