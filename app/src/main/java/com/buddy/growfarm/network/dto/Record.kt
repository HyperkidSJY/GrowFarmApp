package com.buddy.growfarm.network.dto

data class Record(
    val arrival_date: String,
    val commodity: String,
    val district: String,
    val grade: String,
    val market: String,
    val max_price: String,
    val min_price: String,
    val modal_price: String,
    val state: String,
    val variety: String
)