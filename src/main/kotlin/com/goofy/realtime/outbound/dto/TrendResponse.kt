package com.goofy.realtime.outbound.dto

import com.goofy.realtime.domain.trend.vo.TrendId
import com.goofy.realtime.inbound.mysql.entity.Trend
import java.time.LocalDateTime

data class TrendResponse(
    val id: TrendId,
    val title: String,
    val content: String,
    val seq: Int,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    companion object {
        fun from(trend: Trend): TrendResponse {
            return TrendResponse(
                id = trend.id,
                title = trend.title,
                content = trend.content,
                seq = trend.seq,
                createdAt = trend.createdAt,
                updatedAt = trend.updatedAt
            )
        }
    }
}
