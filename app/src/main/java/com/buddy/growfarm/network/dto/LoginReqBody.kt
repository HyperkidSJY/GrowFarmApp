package com.buddy.growfarm.network.dto

import com.google.gson.annotations.SerializedName

data class LoginReqBody(
    val phoneNumber : String,
    val password : String
)