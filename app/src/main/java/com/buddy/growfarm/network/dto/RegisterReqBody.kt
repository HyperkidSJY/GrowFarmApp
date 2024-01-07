package com.buddy.growfarm.network.dto

data class RegisterReqBody(
    val phoneNumber: String,
    val password: String,
    val gmail: String,
    val name: String
)