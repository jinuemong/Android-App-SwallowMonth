package com.jinuemong.SwallowMonthJM

import android.content.Context
import android.content.Intent
import androidx.work.*
import com.jinuemong.SwallowMonthJM.MainActivity.Companion.REQUEST_CODE_1
import com.jinuemong.SwallowMonthJM.MainActivity.Companion.REQUEST_CODE_2
import kotlinx.coroutines.coroutineScope
import java.util.Calendar

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
            }
    }

    private fun monthBroadIntent() : Intent{
         return Intent(applicationContext,AlarmBroadCastReceiver::class.java)
            .let { intent ->
                intent.putExtra("code", REQUEST_CODE_2)
            }
    }
}