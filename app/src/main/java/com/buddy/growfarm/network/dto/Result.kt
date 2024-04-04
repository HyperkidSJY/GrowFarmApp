package com.buddy.growfarm.network.dto

data class Result(
    val _id: String,
    val comments: List<String>,
    val createdAt: String,
    val description: String,
    val likes: List<String>,
    val name: String,
    val postMedia: List<String>,
    val postTitle: String,
    val roles: String,
    var isLiked: Boolean = false
)