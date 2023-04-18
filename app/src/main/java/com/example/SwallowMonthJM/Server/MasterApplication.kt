package com.example.SwallowMonthJM.Server

import android.app.Activity
import android.app.Application
import android.content.Context
import com.example.SwallowMonthJM.Server.TokenManager.AuthInterceptor
import com.example.SwallowMonthJM.Server.TokenManager.TokenRefreshApi
import com.facebook.stetho.Stetho
import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create

class MasterApplication:Application(

) {
    lateinit var service:RetrofitService
    val baseUrl = "https://259d-14-51-88-88.ngrok-free.app"
    private lateinit var activity: Activity
    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
    }
    fun createRetrofit(currentActivity: Activity){
        activity = currentActivity

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

        val retrofit = Retrofit.Builder()
            .baseUrl("$baseUrl/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(getRetrofitClient(header))
            .build()

        service = retrofit.create(RetrofitService::class.java)
    }

    // refresh 토큰을 얻기 위한 일회성 빌더 생성
    private fun buildTokenApi() : TokenRefreshApi{
        // 임시 클라이언트 생성
        val client = OkHttpClient.Builder().build()
        return Retrofit.Builder()
            .baseUrl("$baseUrl/")
            .client(client)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TokenRefreshApi::class.java)
    }

    private fun getRetrofitClient(header:Interceptor) : OkHttpClient{
        return OkHttpClient.Builder()
            .addInterceptor {chain->
                chain.proceed(chain.request().newBuilder().also {
                    it.addHeader("Accept","application/json")
                }.build())
            }.also {client->
                client.addInterceptor(header)
                client.addInterceptor(AuthInterceptor(activity,buildTokenApi()))

                //로그 기록 인터셉터 등록
                val logInterceptor = HttpLoggingInterceptor()
                logInterceptor.level = HttpLoggingInterceptor.Level.BODY
                client.addInterceptor(logInterceptor)
            }.build()

    }
    private fun checkIsLogin() : Boolean{
        val sp = getSharedPreferences("login_sp",Context.MODE_PRIVATE)
        val token = sp.getString("accessToken","null")
        return token!="null"
        //is not default
    }
    private fun getUserToken(): String?{
        val sp = getSharedPreferences("login_sp",Context.MODE_PRIVATE)
        val token = sp.getString("accessToken","null")
        return if (token=="null") null
        else token
    }
}