package com.goofy.realtime.outbound.dto

data class TrendCreateRequest(
    val title: String,
    val content: String,
    val seq: Int
)
