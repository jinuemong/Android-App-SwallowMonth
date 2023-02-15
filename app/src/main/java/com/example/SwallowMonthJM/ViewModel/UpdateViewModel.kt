package com.example.SwallowMonthJM.ViewModel

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.SwallowMonthJM.Model.Profile
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File


// Profile 업데이트 기능

class UpdateViewModel : ViewModel(){

    fun updateProfile(profile:Profile, imageUri : String,
                      activity:Activity,paramFunc:(Profile?)->Unit){
        viewModelScope.launch {
            try{
                //username , userComment 수정
                val unRequestBody : RequestBody = profile.userName.toPlainRequestBody()
                val ucRequestBody : RequestBody = profile.userComment.toPlainRequestBody()

                val imageMultipartList = MultipartBody.Part
                val imageFile = File(imageUri)
                val imRequestBody = imageFile.asRequestBody("image/jpeg".toMediaTypeOrNull())

                ()
            }catch (e : Exception){
                paramFunc(null)
            }
        }
    }



    private fun String?.toPlainRequestBody () =
        requireNotNull(this).toRequestBody("text/plain".toMediaTypeOrNull())

}