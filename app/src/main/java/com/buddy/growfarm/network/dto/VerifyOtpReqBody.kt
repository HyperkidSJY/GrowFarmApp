package com.buddy.growfarm.network.dto

data class VerifyOtpReqBody(
    val phoneNumber : String,
    val otp : String
)