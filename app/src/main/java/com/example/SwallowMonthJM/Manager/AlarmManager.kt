package com.example.SwallowMonthJM.Manager

import com.example.SwallowMonthJM.Model.Alarm
import com.example.SwallowMonthJM.Model.AlarmForGet
import com.example.SwallowMonthJM.Network.MasterApplication
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AlarmManager(
    private val masterApp : MasterApplication

) {

    fun getMyAlarmList(userName: String,paramFunc: (ArrayList<AlarmForGet>?, message: String?) -> Unit){
        masterApp.service.getAlarmList(userName)
            .enqueue((object : Callback<ArrayList<AlarmForGet>> {
                override fun onResponse(
                    call: Call<ArrayList<AlarmForGet>>,
                    response: Response<ArrayList<AlarmForGet>>
                ) {
                    if(response.isSuccessful){
                        paramFunc(response.body(),null)
                    }else{
                        paramFunc(null, response.errorBody()!!.string())
                    }
                }

                override fun onFailure(call: Call<ArrayList<AlarmForGet>>, t: Throwable) {
                    paramFunc(null,"error")
                }

            }))
    }

    fun sendAlarm(targetUser : String, type : String, typeId: Int,paramFunc: (Alarm?, message: String?) -> Unit){
        masterApp.service.addAlarm(targetUser,type,typeId)
            .enqueue(object : Callback<Alarm> {
                override fun onResponse(call: Call<Alarm>, response: Response<Alarm>) {
                    if (response.isSuccessful){
                        paramFunc(response.body(),null)
                    }else{
                        paramFunc(null,response.errorBody()!!.string())
                    }
                }

                override fun onFailure(call: Call<Alarm>, t: Throwable) {
                    paramFunc(null,"error")
                }

            })
    }

    fun delAlarm(alarmId:Int,paramFunc: (Alarm?, message: String?) -> Unit){
        masterApp.service.delAlarm(alarmId)
            .enqueue(object : Callback<Alarm> {
                override fun onResponse(call: Call<Alarm>, response: Response<Alarm>) {
                    if (response.isSuccessful){
                        paramFunc(response.body(),null)
                    }else{
                        paramFunc(null,response.errorBody()!!.string())
                    }
                }

                override fun onFailure(call: Call<Alarm>, t: Throwable) {
                    paramFunc(null,"error")
                }

            })
    }
}