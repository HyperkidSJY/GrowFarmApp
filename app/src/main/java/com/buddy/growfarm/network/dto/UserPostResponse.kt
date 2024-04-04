package com.buddy.growfarm.network.dto

data class UserPostResponse(
    val followers: Int,
    val following: Int,
    val profilePicture: String,
    val result: List<Result>
)