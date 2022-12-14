package com.example.SwallowMonthJM.Server

import android.app.Application
import android.content.Context
import com.facebook.stetho.Stetho
import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class MasterApplication:Application(

) {
    lateinit var service:RetrofitService
    private val baseUrl = "https://6fde-59-14-57-18.jp.ngrok.io"
    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
        createRetrofit()
    }
    private fun createRetrofit(){

        val header = Interceptor{
            val original = it.request()
            if (checkIsLogin()){
                getUserToken().let { token->
                    val request = original.newBuilder()
                        .header("Authorization","token $token")
                        .build()
                    it.proceed(request)
                }
            }else{
                it.proceed(original)
            }
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(header)
            .addNetworkInterceptor(StethoInterceptor())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("$baseUrl/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        service = retrofit.create(RetrofitService::class.java)
    }
    private fun checkIsLogin() : Boolean{
        val sp = getSharedPreferences("login_sp",Context.MODE_PRIVATE)
        val token = sp.getString("login_sp","null")
        return token!="null"
        //is not default
    }
    private fun getUserToken(): String?{
        val sp = getSharedPreferences("login_sp",Context.MODE_PRIVATE)
        val token = sp.getString("login_sp","null")
        return if (token=="null") null
        else token
    }
}