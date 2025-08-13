package com.goofy.realtime.inbound.dto

data class TrendCreateRequest(
    val title: String,
    val content: String,
    val seq: Int
)
