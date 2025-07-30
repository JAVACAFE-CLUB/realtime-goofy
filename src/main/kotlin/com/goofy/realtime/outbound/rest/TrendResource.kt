package com.goofy.realtime.outbound.rest

import com.goofy.realtime.common.dto.PageRequest
import com.goofy.realtime.common.dto.PageResponse
import com.goofy.realtime.common.dto.Response
import com.goofy.realtime.common.extension.wrapOk
import com.goofy.realtime.common.extension.wrapPage
import com.goofy.realtime.common.extension.wrapVoid
import com.goofy.realtime.domain.trend.application.TrendService
import com.goofy.realtime.domain.trend.vo.TrendId
import com.goofy.realtime.outbound.dto.TrendCreateRequest
import com.goofy.realtime.outbound.dto.TrendResponse
import com.goofy.realtime.outbound.dto.TrendUpdateRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springdoc.core.annotations.ParameterObject
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Trend API")
@RestController
@RequestMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
class TrendResource(
    private val trendService: TrendService
) {
    @Operation(summary = "트렌드 생성")
    @PostMapping("/api/v1/trends")
    suspend fun createTrend(
        @RequestBody request: TrendCreateRequest
    ): ResponseEntity<Response<TrendResponse>> {
        return trendService.createTrend(request)
            .let { trend -> TrendResponse.from(trend) }
            .wrapOk()
    }

    @Operation(summary = "트렌드 목록 조회")
    @GetMapping("/api/v1/trends")
    suspend fun getTrends(
        @ParameterObject pageRequest: PageRequest,
    ): PageResponse<TrendResponse> {
        return trendService.getTrends(pageRequest)
            .map { trend -> TrendResponse.from(trend) }
            .wrapPage()
    }

    @Operation(summary = "트렌드 단건 조회")
    @GetMapping("/api/v1/trends/{id}")
    suspend fun getTrend(
        @PathVariable id: Long
    ): ResponseEntity<Response<TrendResponse>> {
        return trendService.getTrend(TrendId(id))
            .let { trend -> TrendResponse.from(trend) }
            .wrapOk()
    }

    @Operation(summary = "트렌드 수정")
    @PutMapping("/api/v1/trends/{id}")
    suspend fun updateTrend(
        @PathVariable id: TrendId,
        @RequestBody request: TrendUpdateRequest
    ): ResponseEntity<Response<TrendResponse>> {
        return trendService.updateTrend(id, request)
            .let { trend -> TrendResponse.from(trend) }
            .wrapOk()
    }

    @Operation(summary = "트렌드 삭제")
    @DeleteMapping("/api/v1/trends/{id}")
    suspend fun deleteTrend(
        @PathVariable id: TrendId
    ): ResponseEntity<Unit> {
        trendService.deleteTrend(id)
        return Unit.wrapVoid()
    }
}
