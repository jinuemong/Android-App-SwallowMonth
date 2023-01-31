package com.example.SwallowMonthJM.Manager

import com.example.SwallowMonthJM.Model.DayRoutine
import com.example.SwallowMonthJM.Model.Routine
import com.example.SwallowMonthJM.Network.MasterApplication
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RoutineManager(
    private val materApp : MasterApplication
) {

    fun getRoutineList(userName:String,keyDate:String,paramFun: (ArrayList<Routine>?) -> Unit){
        materApp.service.getMonthRoutineList(userName,keyDate)
            .enqueue(object : Callback<ArrayList<Routine>>{
                override fun onResponse(
                    call: Call<ArrayList<Routine>>,
                    response: Response<ArrayList<Routine>>
                ) {
                    if(response.isSuccessful){
                        paramFun(response.body())
                    }else{
                        paramFun(null)
                    }
                }

                override fun onFailure(call: Call<ArrayList<Routine>>, t: Throwable) {
                    paramFun(null)
                }

            })
    }
    fun addRoutine(routine: Routine,paramFun:(Routine?)->Unit){
        materApp.service.addRoutine(routine.userId,routine.monthId,routine.keyDate,routine.text,
        routine.cycle,routine.startNum,routine.totalRoutine,routine.clearRoutine,routine.iconType,routine.topText)
            .enqueue(object :Callback<Routine>{
                override fun onResponse(call: Call<Routine>, response: Response<Routine>) {
                    if(response.isSuccessful){
                        paramFun(response.body())
                    }else{
                        paramFun(null)
                    }
                }

                override fun onFailure(call: Call<Routine>, t: Throwable) {
                    paramFun(null)
                }

            })
    }

    fun delRoutine(routineId:Int,paramFun: (Routine?) -> Unit){
        materApp.service.delRoutine(routineId).enqueue(object :Callback<Routine>{
            override fun onResponse(call: Call<Routine>, response: Response<Routine>) {
                if(response.isSuccessful){
                    paramFun(response.body())
                }else{
                    paramFun(null)
                }
            }

            override fun onFailure(call: Call<Routine>, t: Throwable) {
                paramFun(null)
            }

        })
    }

    fun addDayRoutine(dayRoutine: DayRoutine,paramFun: (DayRoutine?) -> Unit){
        materApp.service.addDayRoutine(dayRoutine.routineId,dayRoutine.monthId,dayRoutine.dayIndex,dayRoutine.clear)
            .enqueue(object : Callback<DayRoutine>{
                override fun onResponse(call: Call<DayRoutine>, response: Response<DayRoutine>) {
                    if(response.isSuccessful){
                        paramFun(response.body())
                    }else{
                        paramFun(null)
                    }
                }
                override fun onFailure(call: Call<DayRoutine>, t: Throwable) {
                    paramFun(null)
                }
            })
    }
}