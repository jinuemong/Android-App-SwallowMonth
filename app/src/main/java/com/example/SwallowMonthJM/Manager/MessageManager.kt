package com.example.SwallowMonthJM.Manager

import com.example.SwallowMonthJM.Model.Message
import com.example.SwallowMonthJM.Network.MasterApplication
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


}