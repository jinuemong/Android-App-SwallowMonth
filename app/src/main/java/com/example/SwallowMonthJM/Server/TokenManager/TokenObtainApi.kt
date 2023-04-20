package com.example.SwallowMonthJM.Server.TokenManager

import com.example.SwallowMonthJM.Model.Token
import retrofit2.http.GET
import retrofit2.http.POST

interface TokenObtainApi {
    @GET("user/auth/token")
    suspend fun getToken(): Token
}