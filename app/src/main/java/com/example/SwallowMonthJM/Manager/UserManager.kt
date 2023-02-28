package com.example.SwallowMonthJM.Manager

import android.net.Uri
import com.example.SwallowMonthJM.MainActivity
import com.example.SwallowMonthJM.Model.Profile
import com.example.SwallowMonthJM.Network.MasterApplication
import com.example.SwallowMonthJM.ViewModel.MultiPartViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserManager(
    private val materApp : MasterApplication,
    private val mainActivity: MainActivity
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

    fun setUserProfile(profile: Profile, imageUri: Uri?, paramFun: (Profile?, String) -> Unit){
        mainActivity.multiPartViewModel
            .updateProfile(profile,imageUri,mainActivity, paramFunc = { reProfile,message->
                paramFun(reProfile,message) //update profile, message
        })
    }
}