package com.example.SwallowMonthJM.Manager

import com.example.SwallowMonthJM.Model.DayData
import com.example.SwallowMonthJM.Server.MasterApplication
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DayDateManager(
    private val materApp : MasterApplication
) {

    //전체 dayData 받기
    fun getTotalDayData(userName:String,paramFun:(ArrayList<DayData>?)->Unit){
        materApp.service.getDayDataList(userName)
            .enqueue(object : Callback<ArrayList<DayData>>{
                override fun onResponse(
                    call: Call<ArrayList<DayData>>,
                    response: Response<ArrayList<DayData>>
                ) {
                    if(response.isSuccessful) {
                        paramFun(response.body())
                    }else(paramFun(null))
                }
                override fun onFailure(call: Call<ArrayList<DayData>>, t: Throwable) {
                    paramFun(null)
                }

            })
    }

    //KeyDate으로 dayData 받기
    fun getKeyDateDayData(userName:String,KeyDate:String
                          ,paramFun: (ArrayList<DayData>?) -> Unit){
        materApp.service.getKeyDate(userName,KeyDate)
            .enqueue(object : Callback<ArrayList<DayData>>{
                override fun onResponse(
                    call: Call<ArrayList<DayData>>,
                    response: Response<ArrayList<DayData>>
                ) {
                    if(response.isSuccessful && response.body()!=null){
                        paramFun(response.body())
                    }else{
                        paramFun(null)
                    }
                }
                override fun onFailure(call: Call<ArrayList<DayData>>, t: Throwable) {
                    paramFun(null)
                }
            })
    }
}