package com.jinuemong.SwallowMonthJM.Manager

import android.net.Uri
import com.jinuemong.SwallowMonthJM.MainActivity
import com.jinuemong.SwallowMonthJM.Model.Profile
import com.jinuemong.SwallowMonthJM.Model.User
import com.jinuemong.SwallowMonthJM.Server.MasterApplication
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

    fun updateUserPassword(userName: String, password:String,paramFun: (User?, message: String?) -> Unit){
        materApp.service.updatePassword(userName,password)
            .enqueue(object : Callback<User>{
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful){
                        paramFun(response.body(),null)
                    }else{
                        paramFun(null, response.errorBody()!!.string())
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    paramFun(null,"error: $t")
                }

            })
    }
}