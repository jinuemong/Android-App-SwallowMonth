package com.jinuemong.SwallowMonthJM.Manager

import com.jinuemong.SwallowMonthJM.Model.*
import com.jinuemong.SwallowMonthJM.Server.MasterApplication
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RelationManager(
    private val masterApp:MasterApplication
) {

    fun makeNewFriendRelation(userName:String,otherUser:Int,targetUser: String
                              ,paramFunc: (FriendShip?, message: String?) -> Unit){
        // make friend ship - user - alarm
        addFriendRelation("$userName make FriendShip!",paramFunc={ friendShip, message ->
            if (message==null){
                addFUser(friendShip!!.frId,userName,otherUser, paramFunc = { _,_-> })
                AlarmManager(masterApp).sendAlarm(userName,targetUser,"FriendShip",friendShip.frId,
                    paramFunc = {_,_-> })
                paramFunc(friendShip,null)
            }else{paramFunc(null,"error")}
        })
    }
    private fun addFriendRelation(name : String, paramFunc:(FriendShip?, message:String?)->Unit){
        masterApp.service.addFriendShip(name)
            .enqueue(object : Callback<FriendShip>{
                override fun onResponse(call: Call<FriendShip>, response: Response<FriendShip>) {
                    if (response.isSuccessful){
                        paramFunc(response.body(),null)
                    }else{
                        paramFunc(null, response.errorBody()!!.string())

                    }
                }

                override fun onFailure(call: Call<FriendShip>, t: Throwable) {
                    paramFunc(null,"error")
                }

            })
    }

    fun checkFriend(userName:String, targetUser: Int,paramFunc: (CheckRelation?, message: String?) -> Unit){
        masterApp.service.checkFriendShip(userName,targetUser)
            .enqueue(object : Callback<CheckRelation>{
                override fun onResponse(call: Call<CheckRelation>, response: Response<CheckRelation>) {
                    if (response.isSuccessful){
                        paramFunc(response.body()!!,null)
                    }else{
                        paramFunc(null,"error:"+response.errorBody()!!.string())
                    }
                }
                override fun onFailure(call: Call<CheckRelation>, t: Throwable) {
                    paramFunc(null,"error")

                }

            })
    }
    fun addFUser(frId:Int, userId:String, otherUser:Int, paramFunc: (FUser?, message: String?) -> Unit){
        masterApp.service.addFUser(frId,userId,otherUser)
            .enqueue(object :  Callback<FUser>{
                override fun onResponse(call: Call<FUser>, response: Response<FUser>) {
                    if (response.isSuccessful){
                        paramFunc(response.body(),null)
                    }else{
                        paramFunc(null,response.errorBody()!!.string())
                    }
                }

                override fun onFailure(call: Call<FUser>, t: Throwable) {
                    paramFunc(null, "error")
                }

            })
    }

    fun getFriendShip(frId: Int,paramFunc: (FriendShip?, message: String?) -> Unit){
        masterApp.service.getFriendShip(frId)
            .enqueue(object : Callback<FriendShip>{
                override fun onResponse(call: Call<FriendShip>, response: Response<FriendShip>) {
                    if (response.isSuccessful){
                        paramFunc(response.body(),null)
                    }else{
                        paramFunc(null,response.errorBody()!!.string())
                    }
                }

                override fun onFailure(call: Call<FriendShip>, t: Throwable) {
                    paramFunc(null,"error")
                }

            })
    }

    fun delFriendShip(frId : Int, paramFunc: (FriendShip?, message: String?) -> Unit){
        masterApp.service.delFriendShip(frId)
            .enqueue(object : Callback<FriendShip>{
                override fun onResponse(call: Call<FriendShip>, response: Response<FriendShip>) {
                    if (response.isSuccessful){
                        paramFunc(response.body(),null)
                    }else{
                        paramFunc(null, response.errorBody()!!.string())
                    }
                }

                override fun onFailure(call: Call<FriendShip>, t: Throwable) {
                    paramFunc(null,"error")
                }

            })
    }

    fun getFriendListR(userName: String, num:Int, paramFunc: (FriendList?, message: String?) -> Unit){
        masterApp.service.getFriends(userName,num)
            .enqueue(object : Callback<FriendList>{
                override fun onResponse(
                    call: Call<FriendList>,
                    response: Response<FriendList>
                ) {
                    if(response.isSuccessful){
                        paramFunc(response.body(),null)
                    }else{
                        paramFunc(null,response.errorBody()!!.string())
                    }
                }

                override fun onFailure(call: Call<FriendList>, t: Throwable) {
                    paramFunc(null,"error")
                }

            })
    }

    fun getRandomProfileList(profileId : Int,paramFunc: (ArrayList<Profile>?, message: String?) -> Unit){
        masterApp.service.getRandomProfile(profileId)
            .enqueue(object : Callback<ArrayList<Profile>>{
                override fun onResponse(
                    call: Call<ArrayList<Profile>>,
                    response: Response<ArrayList<Profile>>
                ) {
                    if(response.isSuccessful){
                        paramFunc(response.body(),null)
                    }else{
                        paramFunc(null,response.errorBody()!!.string())
                    }
                }

                override fun onFailure(call: Call<ArrayList<Profile>>, t: Throwable) {
                    paramFunc(null,"error")
                }

            })
    }



}