package com.jinuemong.SwallowMonthJM.Server.TokenManager

import com.jinuemong.SwallowMonthJM.Model.Token
import retrofit2.http.GET

interface TokenObtainApi {
    @GET("user/auth/token")
    suspend fun getToken(): Token
}