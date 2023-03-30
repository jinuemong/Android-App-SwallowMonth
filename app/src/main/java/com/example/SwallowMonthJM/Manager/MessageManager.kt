package com.example.SwallowMonthJM.Manager

import com.example.SwallowMonthJM.Model.FriendData
import com.example.SwallowMonthJM.Model.Message
import com.example.SwallowMonthJM.Server.MasterApplication
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MessageManager(
    private val masterApp : MasterApplication

) {
    fun sendMessage(frId:Int, userId : String,text:String,
    paramFunc : (Message?, errMessage:String?)->Unit){
        masterApp.service.postMessage(frId, userId, text)
            .enqueue(object :Callback<Message>{
                override fun onResponse(call: Call<Message>, response: Response<Message>) {
                    if (response.isSuccessful){
                        paramFunc(response.body(),null)
                    }else{
                        paramFunc(null, response.errorBody()!!.string())
                    }
                }

                override fun onFailure(call: Call<Message>, t: Throwable) {
                    paramFunc(null,"error")
                }

            })
    }

    fun delMessage(messageId : Int,paramFunc: (Message?, errMessage: String?) -> Unit){
        masterApp.service.deleteMessage(messageId)
            .enqueue(object : Callback<Message>{
                override fun onResponse(call: Call<Message>, response: Response<Message>) {
                    if (response.isSuccessful){
                        paramFunc(response.body(),null)
                    }else{
                        paramFunc(null,response.errorBody()!!.string())
                    }
                }

                override fun onFailure(call: Call<Message>, t: Throwable) {
                    paramFunc(null,"error")
                }

            })
    }


    // 해당 유저와의 메시지 리스트
    fun getMessageList(frId: Int,paramFunc: (ArrayList<Message>?, errMessage: String?) -> Unit){
        masterApp.service.getMessageList(frId)
            .enqueue(object : Callback<ArrayList<Message>>{
                override fun onResponse(
                    call: Call<ArrayList<Message>>,
                    response: Response<ArrayList<Message>>
                ) {
                    if (response.isSuccessful){
                        paramFunc(response.body(),null)
                    }else{
                        paramFunc(null,response.errorBody()!!.string())
                    }
                }

                override fun onFailure(call: Call<ArrayList<Message>>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
    }

    // 메시지 룸 리스트 받기
    fun getMessageRoomList(userName:String,paramFunc:(ArrayList<FriendData>?,message:String?)->Unit){
        masterApp.service.getMessageRoomList(userName)
            .enqueue(object : Callback<ArrayList<FriendData>>{
                override fun onResponse(
                    call: Call<ArrayList<FriendData>>,
                    response: Response<ArrayList<FriendData>>
                ) {
                    if (response.isSuccessful){
                        paramFunc(response.body(),null)
                    }else{
                        paramFunc(null,response.errorBody()!!.string())
                    }
                }

                override fun onFailure(call: Call<ArrayList<FriendData>>, t: Throwable) {
                    paramFunc(null,"error")
                }

            })
    }
}