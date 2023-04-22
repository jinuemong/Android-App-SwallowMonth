package com.jinuemong.SwallowMonthJM.Model

import java.io.Serializable

class AuthUser(
    val user: User,
    val token: Token,
) : Serializable

class GetUser(
    val user : AuthUser
): Serializable