package com.example.SwallowMonthJM.ViewModel

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.SwallowMonthJM.Model.Profile
import com.example.SwallowMonthJM.Network.MasterApplication
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


// Profile 업데이트 기능

class MultiPartViewModel : ViewModel(){

    fun updateProfile(profile:Profile, imageUri : String,
                      activity:Activity,paramFunc:(Profile?,String)->Unit){
        viewModelScope.launch {
            try{
                //username , userComment 수정
                val unRequestBody : RequestBody = profile.userName.toPlainRequestBody()
                val ucRequestBody : RequestBody = profile.userComment.toPlainRequestBody()

                val imageFile = File(imageUri)
                val imRequestBody = imageFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val imageMultipartBody : MultipartBody.Part =
                    MultipartBody.Part.createFormData(
                        imageFile.name,
                        imageFile.name,
                        imRequestBody
                    )
                (activity.application as MasterApplication).service
                    .setUserProfile(profile.profileId,unRequestBody,
                    ucRequestBody,imageMultipartBody).enqueue(object : Callback<Profile> {
                        override fun onResponse(call: Call<Profile>, response: Response<Profile>) {
                            if (response.isSuccessful){
                                paramFunc(response.body(),"")
                            }else{
                                paramFunc(null,response.errorBody()?.string()!!)
                            }
                        }

                        override fun onFailure(call: Call<Profile>, t: Throwable) {
                            paramFunc(null,"")
                        }

                    })
            }catch (e : Exception){
                paramFunc(null,"")
            }
        }
    }



    private fun String?.toPlainRequestBody () =
        requireNotNull(this).toRequestBody("text/plain".toMediaTypeOrNull())

}