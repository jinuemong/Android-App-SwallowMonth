package com.jinuemong.SwallowMonthJM.Model

import java.io.Serializable

class FriendShip (
    val frId : Int,
    val name : String,
    val fUserPost : ArrayList<FUser>,
): Serializable