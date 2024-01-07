package com.buddy.growfarm.network.dto

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("_id")
    val id : String,
    val name : String,
    val phoneNumber : String,
    val createdAt : String,
    val updatedAt : String
)