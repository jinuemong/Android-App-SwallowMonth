package com.example.SwallowMonthJM.Server.TokenManager

import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.SwallowMonthJM.LoginActivity
import com.example.SwallowMonthJM.Model.Token
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val context: Context,
    private val tokenApi: TokenRefreshApi,
    private val obtainApi: TokenObtainApi,
):Interceptor,BaseRepository(){
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        if (response.code==401){// 코드로 토큰 재인증 여부 확인
            return runBlocking {
                Log.d("test 123123123 1",response.body.toString())
                when(val token = getUpdateToken()){
                    is Resource.Success ->{
                        val sp = context.getSharedPreferences("login_sp",Context.MODE_PRIVATE)
                        val editor = sp.edit()
                        val accessToken = token.value!!.access
                        val refreshToken = token.value.refresh
                        Log.d("test 123123123 2",accessToken)
                        editor.putString("accessToken",accessToken)
                        editor.putString("refreshToken",refreshToken)
                        editor.apply()

                        // 기존 토큰 지우고 새로운 response 반환
                        val newRequest = chain.request().newBuilder().removeHeader("Authorization")
                        newRequest.addHeader("Authorization","Bearer $accessToken")
                        return@runBlocking chain.proceed(newRequest.build())
                    }
                    else ->{ // refresh 토큰이 만료 된 경우 -> 새로운 토큰 발급
                        when (val newToken = newToken()) {
                            is Resource.Success-> {
                                val sp = context.getSharedPreferences("login_sp", Context.MODE_PRIVATE)
                                val editor = sp.edit()
                                val accessToken = newToken.value!!.access
                                val refreshToken = newToken.value.refresh
                                editor.putString("accessToken", accessToken)
                                editor.putString("refreshToken", refreshToken)
                                editor.apply()
                                // 기존 토큰 지우고 새로운 response 반환
                                val newRequest = chain.request().newBuilder().removeHeader("Authorization")
                                newRequest.addHeader("Authorization", "Bearer $accessToken")
                                return@runBlocking chain.proceed(newRequest.build())
                            }
                            else-> { //모든 케이스 실패 -> 로그아웃
                                val intent = Intent(context,LoginActivity::class.java)
                                intent.putExtra("logout",true)
                                context.startActivity(intent)
                            }
                        }
                        return@runBlocking response
                    }
                }
            }
        }

        return response //정상 값 반환
    }

    private suspend fun getUpdateToken() : Resource<Token?> {
        val refreshToken = context.getSharedPreferences("login_sp",Context.MODE_PRIVATE)
            .getString("refreshToken","").toString()
        //safeApiCall을 통한 api 요청
        // refresh token이 비었을 경우에는 null 전송을 통해서 에러 반환을 받음
        return safeApiCall { tokenApi.patchToken(refreshToken) }
    }

    private suspend fun newToken() : Resource<Token?>{
        // 완전히 새로운 토큰 반환
        return safeApiCall { obtainApi.getToken() }
    }

}