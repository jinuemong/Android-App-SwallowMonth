package com.example.SwallowMonthJM.Server

import com.example.SwallowMonthJM.Model.MonthData
import com.example.SwallowMonthJM.Model.Profile
import com.example.SwallowMonthJM.Model.User
import retrofit2.Call
import retrofit2.http.*

//url 관리 
interface RetrofitService {

    //로그인 요청
    @POST("user/login/")
    @FormUrlEncoded
    fun loginUser(
        @Field("userName") userName:String,
        @Field("password") password : String
    ) : Call<User>

    // 가입
    @POST("user/register/")
    @FormUrlEncoded
    fun registerUser(
        @Field("userName") userName: String,
        @Field("password") password : String
    ): Call<User>

    // 유저 조회
    @GET("user/profile/")
    fun getProfile(
        @Query(value = "search",encoded = true)userName: String
    ): Call<ArrayList<Profile>>

    // 전체 monthData 받기
    @GET("month/monthDatas/")
    fun getDayDataList(
        @Query(value = "userName",encoded = true)userName: String
    ): Call<ArrayList<MonthData>>

    //특정 dayData 받기 (1개)
    @GET("month/monthDatas/")
    fun getKeyDate(
        @Query(value = "userName",encoded = true)userName: String,
        @Query(value = "KeyDate",encoded = true)KeyDate: String
    ): Call<ArrayList<MonthData>>
}