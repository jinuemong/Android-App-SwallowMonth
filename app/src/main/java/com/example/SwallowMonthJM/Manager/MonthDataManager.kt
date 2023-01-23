package com.example.SwallowMonthJM.Manager

import com.example.SwallowMonthJM.Model.MonthData
import com.example.SwallowMonthJM.Server.MasterApplication
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MonthDataManager(
    private val materApp : MasterApplication
) {

    //전체 MonthData 받기
    fun getTotalMonthData(userName:String,paramFun:(ArrayList<MonthData>?)->Unit){
        materApp.service.getMonthDataList(userName)
            .enqueue(object : Callback<ArrayList<MonthData>>{
                override fun onResponse(
                    call: Call<ArrayList<MonthData>>,
                    response: Response<ArrayList<MonthData>>
                ) {
                    if(response.isSuccessful) {
                        paramFun(response.body())
                    }else(paramFun(null))
                }
                override fun onFailure(call: Call<ArrayList<MonthData>>, t: Throwable) {
                    paramFun(null)
                }

            })
    }

    //KeyDate로 특정 달 MonthData 받기
    fun getKeyDateMonthData(userName:String,KeyDate:String
                          ,paramFun: (ArrayList<MonthData>?) -> Unit){
        materApp.service.getMonthKeyDate(userName,KeyDate)
            .enqueue(object : Callback<ArrayList<MonthData>>{
                override fun onResponse(
                    call: Call<ArrayList<MonthData>>,
                    response: Response<ArrayList<MonthData>>
                ) {
                    if(response.isSuccessful && response.body()!=null){
                        paramFun(response.body())
                    }else{
                        paramFun(null)
                    }
                }
                override fun onFailure(call: Call<ArrayList<MonthData>>, t: Throwable) {
                    paramFun(null)
                }
            })
    }

    //새로 MonthData 생성 -> routine이나 task 데이터 생성 시 둘 다 0이면 새로운 달 생성
    fun addMonthData(monthData: MonthData,paramFun:(MonthData?)->Unit){
        materApp.service.makeMonthData(monthData.userId,monthData.keyDate,monthData.totalPer,monthData.totalPoint,
        monthData.taskCount,monthData.dayRoutineCount,monthData.doneTask,monthData.clearRoutine)
            .enqueue(object : Callback<MonthData>{
                override fun onResponse(call: Call<MonthData>, response: Response<MonthData>) {
                    if(response.isSuccessful && response.body()!=null){
                        paramFun(response.body())
                    }else{
                        paramFun(null)
                    }
                }

                override fun onFailure(call: Call<MonthData>, t: Throwable) {
                    paramFun(null)
                }
            })
    }

    // MonthData 제거
    fun delMonthData(monthId: Int,paramFun:(MonthData?)->Unit){
        materApp.service.delMonthData(monthId)
            .enqueue(object :Callback<MonthData>{
                override fun onResponse(call: Call<MonthData>, response: Response<MonthData>) {
                    if(response.isSuccessful && response.body()!=null){
                        paramFun(response.body())
                    }else{
                        paramFun(null)
                    }
                }

                override fun onFailure(call: Call<MonthData>, t: Throwable) {
                    paramFun(null)
                }

            })
    }
}