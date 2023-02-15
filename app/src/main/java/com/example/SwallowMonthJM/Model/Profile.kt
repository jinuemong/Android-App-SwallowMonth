package com.example.SwallowMonthJM.Model

import java.io.Serializable

class Profile(
    val profileId : Int,
    val userName : String,
    val userImage : String,
    val userComment : String,
) : Serializable