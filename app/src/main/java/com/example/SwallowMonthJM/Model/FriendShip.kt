package com.example.SwallowMonthJM.Model

import java.io.Serializable

class FriendShip (
    val frId : Int,
    val name : String,
    val fUser : ArrayList<FUser>,
): Serializable