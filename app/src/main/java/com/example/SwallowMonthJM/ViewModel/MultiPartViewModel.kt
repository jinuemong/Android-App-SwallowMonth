package com.example.SwallowMonthJM.ViewModel

import android.annotation.SuppressLint
import android.app.Activity
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.SwallowMonthJM.Model.Profile
import com.example.SwallowMonthJM.Server.MasterApplication
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

    fun updateProfile(profile:Profile, imagePath : Uri?,
                      activity:Activity, paramFunc:(Profile?,String)->Unit){
        viewModelScope.launch {
            try{
                val idRequestBody : RequestBody = profile.profileId.toString().toPlainRequestBody()
                //username , userComment 수정
                val unRequestBody : RequestBody = profile.userName.toPlainRequestBody()
                val ucRequestBody : RequestBody = profile.userComment.toPlainRequestBody()
                var imageMultipartBody:MultipartBody.Part?=null

                //이미지 있으면 변환
                if (imagePath!=null) {
                    val path = getImageFilePath(activity, imagePath)
                    val imageFile = File(path)
                    val imRequestBody = imageFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
                    imageMultipartBody =
                        MultipartBody.Part.createFormData(
                            "userImage",
                            imageFile.name,
                            imRequestBody
                        )
                }

                (activity.application as MasterApplication).service
                    .setUserProfile(idRequestBody,unRequestBody,
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

    @SuppressLint("Recycle")
    private fun getImageFilePath(activity: Activity,uri: Uri) : String{
        var columIndex = 0
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = activity.contentResolver.query(uri,projection,null,null,null)
        if(cursor!!.moveToFirst()){
            columIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        }

        return cursor.getString(columIndex)
    }

}