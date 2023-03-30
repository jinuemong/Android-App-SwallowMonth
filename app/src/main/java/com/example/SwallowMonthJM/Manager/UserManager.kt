package com.example.SwallowMonthJM.Manager

import android.net.Uri
import com.example.SwallowMonthJM.MainActivity
import com.example.SwallowMonthJM.Model.Profile
import com.example.SwallowMonthJM.Server.MasterApplication
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserManager(
    private val materApp : MasterApplication,
    private val mainActivity: MainActivity
) {
    fun getUserProfile(profileId:Int, paramFun:(Profile?,message:String?)->Unit){
        materApp.service.getProfile(profileId)
            .enqueue(object : Callback<Profile> {
                override fun onResponse(call: Call<Profile>, response: Response<Profile>) {
                    if(response.isSuccessful && response.body()!=null){
                        val profile  =response.body()
                        paramFun(profile,null)
                    }else{
                        paramFun(null,response.errorBody()!!.string())
                    }
                }
                override fun onFailure(call: Call<Profile>, t: Throwable) {
                    paramFun(null,"error")
                }
            })
    }

    fun getUserProfileWithUserName(username:String,paramFun: (Profile?, message: String?) -> Unit){
        materApp.service.searchProfile(username)
            .enqueue(object : Callback<ArrayList<Profile>>{
                override fun onResponse(
                    call: Call<ArrayList<Profile>>,
                    response: Response<ArrayList<Profile>>
                ) {
                    if (response.isSuccessful && response.body()!!.size>0){
                        paramFun(response.body()!![0],null)
                    }else{
                        paramFun(null,response.errorBody()!!.string())
                    }
                }

                override fun onFailure(call: Call<ArrayList<Profile>>, t: Throwable) {
                    paramFun(null,"error")
                }

            })
    }

    fun searchUserProfiles(param : String, paramFun: (ArrayList<Profile>?, message: String?) -> Unit){
        materApp.service.searchProfile(param)
            .enqueue(object : Callback<ArrayList<Profile>>{
                override fun onResponse(
                    call: Call<ArrayList<Profile>>,
                    response: Response<ArrayList<Profile>>
                ) {
                    if (response.isSuccessful){
                        paramFun(response.body(),null)
                    }else{
                        paramFun(null,response.errorBody()!!.string())
                    }
                }

                override fun onFailure(call: Call<ArrayList<Profile>>, t: Throwable) {
                    paramFun(null,"error")
                }

            })
    }
    fun setUserProfile(profile: Profile, imageUri: Uri?, paramFun: (Profile?, String) -> Unit){
        mainActivity.multiPartViewModel
            .updateProfile(profile,imageUri,mainActivity, paramFunc = { reProfile,message->
                paramFun(reProfile,message) //update profile, message
        })
    }
}