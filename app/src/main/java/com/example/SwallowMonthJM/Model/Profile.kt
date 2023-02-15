package com.example.SwallowMonthJM.Model

import java.io.Serializable

class Profile(
    val profileId : String,
    val userName : String,
    val userImage : String,
    val userComment : String,
) : Serializable