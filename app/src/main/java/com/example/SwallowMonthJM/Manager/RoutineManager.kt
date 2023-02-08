package com.example.SwallowMonthJM.Manager

import android.util.Log
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
        Log.d("userId : :::::",routine.userId.toString())
        Log.d("monthId : :::::",routine.monthId.toString())
        Log.d("keyDate : :::::",routine.keyDate.toString())
        Log.d("text : :::::",routine.text)
        Log.d("cycle : :::::",routine.cycle.toString())
        Log.d("startNum : :::::",routine.startNum.toString())
        Log.d("totalRoutine : :::::",routine.totalRoutine.toString())
        Log.d("clearRoutine : :::::",routine.clearRoutine.toString())
        Log.d("iconType : :::::",routine.iconType.toString())
        Log.d("topText : :::::",routine.topText.toString())


        materApp.service.addRoutine(routine.userId,routine.monthId,routine.keyDate,routine.text,
        routine.cycle,routine.startNum,routine.totalRoutine,routine.clearRoutine,routine.iconType,routine.topText)
            .enqueue(object :Callback<Routine>{
                override fun onResponse(call: Call<Routine>, response: Response<Routine>) {
                    if(response.isSuccessful){
                        paramFun(response.body())
                    }else{
                        paramFun(null)
                        Log.d("err2",response.errorBody()?.string()!!)
                        Log.d("err3",response.message().toString())
                        Log.d("err4",response.body().toString())
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

    fun setRoutineData(routineId :Int, routine: Routine,paramFun: (Routine?) -> Unit){
        materApp.service.setRoutineData(routineId,routine)
            .enqueue(object :Callback<Routine>{
                override fun onResponse(call: Call<Routine>, response: Response<Routine>) {
                    if (response.isSuccessful){
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

    fun setDayRoutineData(id : Int, dayRoutine: DayRoutine,paramFun: (DayRoutine?) -> Unit){
        materApp.service.setDayRoutineData(id,dayRoutine)
            .enqueue(object :Callback<DayRoutine>{
                override fun onResponse(call: Call<DayRoutine>, response: Response<DayRoutine>) {
                    if (response.isSuccessful){
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