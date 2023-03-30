package com.example.SwallowMonthJM.Server.TokenManager

import com.example.SwallowMonthJM.Model.GetUser
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface LoginApi {
    @POST("user/login/")
    @FormUrlEncoded
    suspend fun login(
        @Field("userName") userName:String,
        @Field("password") password : String,
    ): GetUser
}
