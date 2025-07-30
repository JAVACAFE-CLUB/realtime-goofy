package com.goofy.realtime.outbound.dto

data class TrendUpdateRequest(
    val title: String,
    val content: String,
    val seq: Int
)
