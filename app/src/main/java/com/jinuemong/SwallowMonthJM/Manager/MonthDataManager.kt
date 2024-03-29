package com.jinuemong.SwallowMonthJM.Manager

import com.jinuemong.SwallowMonthJM.Model.MonthData
import com.jinuemong.SwallowMonthJM.Model.RecordData
import com.jinuemong.SwallowMonthJM.Server.MasterApplication
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
                        paramFun(call.execute().body())
                    }else(paramFun(null))
                }
                override fun onFailure(call: Call<ArrayList<MonthData>>, t: Throwable) {
                    paramFun(null)
                }

            })
    }

    //KeyDate로 특정 달 MonthData 받기
    fun getKeyDateMonthData(userName:String,KeyDate:String
                          ,paramFun: (ArrayList<MonthData>?,Boolean) -> Unit){
        materApp.service.getMonthKeyDate(userName,KeyDate)
            .enqueue(object : Callback<ArrayList<MonthData>>{
                override fun onResponse(
                    call: Call<ArrayList<MonthData>>,
                    response: Response<ArrayList<MonthData>>
                ) {
                    if(response.isSuccessful){
                        paramFun(response.body(),true)
                    }else{
                        paramFun(null,false)
                    }
                }
                override fun onFailure(call: Call<ArrayList<MonthData>>, t: Throwable) {
                    paramFun(null,false)
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

    //MonthData 수정
    fun setMonthData(monthId: Int,monthData: MonthData,paramFun:(MonthData?)->Unit){
        materApp.service.setMonthData(monthId,monthData)
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

    //레코드 데이터 얻기
    fun getUserRecordData(userName:String,paramFun: (ArrayList<RecordData>?,String?) -> Unit){
        materApp.service.getRecordDataList(userName)
            .enqueue(object : Callback<ArrayList<RecordData>>{
                override fun onResponse(
                    call: Call<ArrayList<RecordData>>,
                    response: Response<ArrayList<RecordData>>
                ) {
                    if (response.isSuccessful){
                        paramFun(response.body(),null)
                    }else{
                        paramFun(null,response.errorBody()!!.string())
                    }
                }

                override fun onFailure(call: Call<ArrayList<RecordData>>, t: Throwable) {
                    paramFun(null,"err : $t")
                }

            })
    }

    //최근 데이터 얻기
    fun getRecentlyData(userName:String,paramFun: (RecordData?) -> Unit){
        materApp.service.getRecentlyData(userName,"")
            .enqueue(object :Callback<ArrayList<RecordData>>{
                override fun onResponse(
                    call: Call<ArrayList<RecordData>>,
                    response: Response<ArrayList<RecordData>>
                ) {
                    if (response.isSuccessful){
                        response.body()?.let {
                            if (it.size>0){
                                paramFun(it[0])
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<ArrayList<RecordData>>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
    }

    // 랭킹 데이터 얻기
    fun getRankingData(keyDate:String,paramFun: (ArrayList<RecordData>?, String?) -> Unit){
        materApp.service.getRankingDataList(keyDate)
            .enqueue(object : Callback<ArrayList<RecordData>>{
                override fun onResponse(
                    call: Call<ArrayList<RecordData>>,
                    response: Response<ArrayList<RecordData>>
                ) {
                    if (response.isSuccessful){
                        paramFun(response.body(),null)
                    }else{
                        paramFun(null, response.errorBody()!!.string())
                    }
                }

                override fun onFailure(call: Call<ArrayList<RecordData>>, t: Throwable) {
                    paramFun(null,"err: $t")
                }

            })
    }
}