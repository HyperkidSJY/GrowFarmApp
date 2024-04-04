package com.buddy.growfarm.network.dto

data class GetCommentResponse(
    val _id : String,
    val commentDescription : String,
    val username : String
)