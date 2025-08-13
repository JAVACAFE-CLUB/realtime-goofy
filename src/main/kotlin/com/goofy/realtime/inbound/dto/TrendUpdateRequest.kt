package com.goofy.realtime.inbound.dto

data class TrendUpdateRequest(
    val title: String,
    val content: String,
    val seq: Int
)
