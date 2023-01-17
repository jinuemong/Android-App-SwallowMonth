package com.example.SwallowMonthJM.Server

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
    ): Call<Profile>
}