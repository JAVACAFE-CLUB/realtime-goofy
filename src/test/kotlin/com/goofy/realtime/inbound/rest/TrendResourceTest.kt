package com.goofy.realtime.inbound.rest

import com.goofy.realtime.common.dto.PageRequest
import com.goofy.realtime.domain.trend.application.TrendService
import com.goofy.realtime.domain.trend.vo.TrendId
import com.goofy.realtime.inbound.dto.TrendCreateRequest
import com.goofy.realtime.inbound.dto.TrendUpdateRequest
import com.goofy.realtime.outbound.mysql.entity.Trend
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.data.domain.PageImpl
import org.springframework.http.HttpStatus
import java.time.LocalDateTime

class TrendResourceTest {
    private lateinit var trendResource: TrendResource
    private lateinit var trendService: TrendService

    @BeforeEach
    fun setUp() {
        trendService = mockk()
        trendResource = TrendResource(trendService)
    }

    @Test
    fun `createTrend should return a trend response with OK status`() = runTest {
        // Given
        val request = TrendCreateRequest(
            title = "Test Title",
            content = "Test Content",
            seq = 1
        )

        val trend = mockk<Trend> {
            every { title } returns request.title
            every { content } returns request.content
            every { seq } returns request.seq
            every { id } returns TrendId(1L)
            every { createdAt } returns LocalDateTime.now()
            every { updatedAt } returns LocalDateTime.now()
        }

        coEvery {
            trendService.createTrend(request)
        } returns trend

        // When
        val result = trendResource.createTrend(request)

        // Then
        result.statusCode shouldBe HttpStatus.OK
        result.body?.data?.id shouldBe trend.id
        result.body?.data?.title shouldBe trend.title
        result.body?.data?.content shouldBe trend.content
        result.body?.data?.seq shouldBe trend.seq

        coVerify { trendService.createTrend(request) }
    }

    @Test
    fun `getTrends should return a page of trend responses`() = runTest {
        // Given
        val pageRequest = PageRequest(page = 0, size = 10, sort = null)

        val trend = mockk<Trend> {
            every { title } returns "Test Title"
            every { content } returns "Test Content"
            every { seq } returns 1
            every { id } returns TrendId(1L)
            every { createdAt } returns LocalDateTime.now()
            every { updatedAt } returns LocalDateTime.now()
        }

        val trendPage = PageImpl(
            listOf(trend),
            org.springframework.data.domain.PageRequest.of(0, 10),
            1
        )

        coEvery {
            trendService.getTrends(pageRequest)
        } returns trendPage

        // When
        val result = trendResource.getTrends(pageRequest)

        // Then
        result.data.size shouldBe 1
        result.data.first().id shouldBe trend.id
        result.data.first().title shouldBe trend.title
        result.data.first().content shouldBe trend.content
        result.data.first().seq shouldBe trend.seq

        coVerify { trendService.getTrends(pageRequest) }
    }

    @Test
    fun `getTrend should return a trend response with OK status`() = runTest {
        // Given
        val trendId = TrendId(1L)

        val trend = mockk<Trend> {
            every { title } returns "Test Title"
            every { content } returns "Test Content"
            every { seq } returns 1
            every { id } returns trendId
            every { createdAt } returns LocalDateTime.now()
            every { updatedAt } returns LocalDateTime.now()
        }

        coEvery {
            trendService.getTrend(trendId)
        } returns trend

        // When
        val result = trendResource.getTrend(trendId)

        // Then
        result.statusCode shouldBe HttpStatus.OK
        result.body?.data?.id shouldBe trend.id
        result.body?.data?.title shouldBe trend.title
        result.body?.data?.content shouldBe trend.content
        result.body?.data?.seq shouldBe trend.seq

        coVerify { trendService.getTrend(any()) }
    }

    @Test
    fun `updateTrend should return an updated trend response with OK status`() = runTest {
        // Given
        val trendId = TrendId(1L)
        val request = TrendUpdateRequest(
            title = "Updated Title",
            content = "Updated Content",
            seq = 2
        )

        val updatedTrend = mockk<Trend> {
            every { title } returns request.title
            every { content } returns request.content
            every { seq } returns request.seq
            every { id } returns trendId
            every { createdAt } returns LocalDateTime.now()
            every { updatedAt } returns LocalDateTime.now()
        }

        coEvery {
            trendService.updateTrend(trendId, request)
        } returns updatedTrend

        // When
        val result = trendResource.updateTrend(trendId, request)

        // Then
        result.statusCode shouldBe HttpStatus.OK
        result.body?.data?.id shouldBe updatedTrend.id
        result.body?.data?.title shouldBe updatedTrend.title
        result.body?.data?.content shouldBe updatedTrend.content
        result.body?.data?.seq shouldBe updatedTrend.seq

        coVerify { trendService.updateTrend(trendId, request) }
    }

    @Test
    fun `deleteTrend should return a void response with NO_CONTENT status`() = runTest {
        // Given
        val trendId = TrendId(1L)

        coEvery {
            trendService.deleteTrend(trendId)
        } returns Unit

        // When
        val result = trendResource.deleteTrend(trendId)

        // Then
        result.statusCode shouldBe HttpStatus.NO_CONTENT

        coVerify { trendService.deleteTrend(trendId) }
    }
}
