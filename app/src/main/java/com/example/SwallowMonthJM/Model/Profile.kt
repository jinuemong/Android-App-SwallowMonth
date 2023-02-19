package com.example.SwallowMonthJM.Model

import java.io.Serializable

class Profile(
    val profileId : Int,
    var userName : String,
    val userImage : String,
    var userComment : String,
) : Serializable