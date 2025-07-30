package com.goofy.realtime.domain.trend.application

import com.goofy.realtime.common.extension.executes
import com.goofy.realtime.config.database.TransactionTemplates
import com.goofy.realtime.domain.trend.vo.TrendId
import com.goofy.realtime.inbound.mysql.entity.Trend
import com.goofy.realtime.inbound.mysql.repository.TrendJpaRepository
import com.goofy.realtime.outbound.dto.TrendCreateRequest
import com.goofy.realtime.outbound.dto.TrendUpdateRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class TrendService(
    private val trendJpaRepository: TrendJpaRepository,
    private val txTemplates: TransactionTemplates,
) {
    suspend fun createTrend(request: TrendCreateRequest): Trend {
        val trend = Trend(
            title = request.title,
            content = request.content,
            seq = request.seq
        )

        return txTemplates.writer.executes {
            trendJpaRepository.save(trend)
        }
    }

    suspend fun getTrends(): List<Trend> {
        return withContext(Dispatchers.IO) {
            trendJpaRepository.findAll()
        }
    }

    suspend fun getTrend(id: TrendId): Trend {
        return withContext(Dispatchers.IO) {
            trendJpaRepository.findByIdOrNull(id.value)
        } ?: throw NoSuchElementException("Trend not found with id: ${id.value}")
    }

    suspend fun updateTrend(id: TrendId, request: TrendUpdateRequest): Trend {
        val existingTrend = getTrend(id)
        val updatedTrend = existingTrend.apply {
            this.title = request.title
            this.content = request.content
            this.seq = request.seq
        }

        return txTemplates.writer.executes {
            trendJpaRepository.save(updatedTrend)
        }
    }

    suspend fun deleteTrend(id: TrendId) {
        val trend = getTrend(id)

        txTemplates.writer.execute {
            trendJpaRepository.delete(trend)
        }
    }
}
