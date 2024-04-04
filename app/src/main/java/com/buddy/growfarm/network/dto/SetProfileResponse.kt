package com.buddy.growfarm.network.dto

data class SetProfileResponse(
    val _id: String,
    val name: String,
    val phoneNumber: String,
    val gender: String,
    val roles: String,
    val district: String,
    val pincode: Int,
    val createdAt: String,
    val updatedAt: String
)