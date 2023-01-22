package com.example.SwallowMonthJM.Manager

import com.example.SwallowMonthJM.Model.Profile
import com.example.SwallowMonthJM.Server.MasterApplication
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserManager(
    private val materApp : MasterApplication
) {
    fun getUserProfile(userName:String,paramFun:(Profile)->Unit){
        materApp.service.getProfile(userName)
            .enqueue(object : Callback<java.util.ArrayList<Profile>> {
                override fun onResponse(call: Call<java.util.ArrayList<Profile>>, response: Response<java.util.ArrayList<Profile>>) {
                    if(response.isSuccessful && response.body()!=null){
                        val profile  =response.body()!![0]
                        paramFun(profile)
                    }
                }
                override fun onFailure(call: Call<java.util.ArrayList<Profile>>, t: Throwable) {}
            })
    }
}