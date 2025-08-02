package com.goofy.realtime.domain.trend.application

import com.goofy.realtime.common.dto.PageRequest
import com.goofy.realtime.config.database.TransactionTemplates
import com.goofy.realtime.domain.trend.vo.TrendId
import com.goofy.realtime.outbound.mysql.entity.Trend
import com.goofy.realtime.outbound.mysql.repository.TrendJpaRepository
import com.goofy.realtime.inbound.dto.TrendCreateRequest
import com.goofy.realtime.inbound.dto.TrendUpdateRequest
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.transaction.support.TransactionCallback
import org.springframework.transaction.support.TransactionTemplate

class TrendServiceTest {
    private lateinit var trendService: TrendService
    private lateinit var trendJpaRepository: TrendJpaRepository
    private lateinit var txTemplates: TransactionTemplates
    private lateinit var writerTemplate: TransactionTemplate

    @BeforeEach
    fun setUp() {
        trendJpaRepository = mockk()
        writerTemplate = mockk()
        txTemplates = mockk {
            every { writer } returns writerTemplate
        }
        trendService = TrendService(trendJpaRepository, txTemplates)
    }

    @Test
    fun `createTrend should create and return a new trend`() = runTest {
        // Given
        val request = TrendCreateRequest(
            title = "Test Title",
            content = "Test Content",
            seq = 1
        )

        val savedTrend = Trend(
            title = request.title,
            content = request.content,
            seq = request.seq
        )

        val trendSlot = slot<Trend>()

        every {
            writerTemplate.execute<Trend>(any())
        } answers {
            val callback = firstArg() as TransactionCallback<Trend>
            callback.doInTransaction(mockk())
        }

        every {
            trendJpaRepository.save(capture(trendSlot))
        } returns savedTrend

        // When
        val result = trendService.createTrend(request)

        // Then
        result shouldBe savedTrend
        trendSlot.captured.title shouldBe request.title
        trendSlot.captured.content shouldBe request.content
        trendSlot.captured.seq shouldBe request.seq
    }

    @Test
    fun `getTrends should return a page of trends`() = runTest {
        // Given
        val pageRequest = PageRequest(page = 0, size = 10, sort = null)
        val trend = Trend(title = "Test Title", content = "Test Content", seq = 1)
        val trendPage = PageImpl(listOf(trend))

        every {
            trendJpaRepository.findAll(any<Pageable>())
        } returns trendPage

        // When
        val result = trendService.getTrends(pageRequest)

        // Then
        result shouldBe trendPage
        result.content.size shouldBe 1
        result.content.first() shouldBe trend
    }

    @Test
    fun `getTrend should return a trend when it exists`() = runTest {
        // Given
        val trendId = TrendId(1L)
        val trend = Trend(title = "Test Title", content = "Test Content", seq = 1)

        every {
            trendJpaRepository.findByIdOrNull(trendId.value)
        } returns trend

        // When
        val result = trendService.getTrend(trendId)

        // Then
        result shouldBe trend
    }

    @Test
    fun `getTrend should throw NoSuchElementException when trend does not exist`() = runTest {
        // Given
        val trendId = TrendId(1L)

        every {
            trendJpaRepository.findByIdOrNull(trendId.value)
        } returns null

        // When/Then
        val exception = assertThrows<NoSuchElementException> {
            trendService.getTrend(trendId)
        }

        exception.message shouldBe "Trend not found with id: ${trendId.value}"
    }

    @Test
    fun `updateTrend should update and return the trend`() = runTest {
        // Given
        val trendId = TrendId(1L)
        val request = TrendUpdateRequest(
            title = "Updated Title",
            content = "Updated Content",
            seq = 2
        )

        val existingTrend = Trend(
            title = "Original Title",
            content = "Original Content",
            seq = 1
        )

        val updatedTrend = Trend(
            title = request.title,
            content = request.content,
            seq = request.seq
        )

        every {
            trendJpaRepository.findByIdOrNull(trendId.value)
        } returns existingTrend

        every {
            writerTemplate.execute<Trend>(any())
        } answers {
            val callback = firstArg() as TransactionCallback<Trend>
            callback.doInTransaction(mockk())
        }

        every {
            trendJpaRepository.save(any())
        } returns updatedTrend

        // When
        val result = trendService.updateTrend(trendId, request)

        // Then
        result shouldBe updatedTrend
    }

    @Test
    fun `deleteTrend should delete the trend`() = runTest {
        // Given
        val trendId = TrendId(1L)
        val trend = Trend(title = "Test Title", content = "Test Content", seq = 1)

        every {
            trendJpaRepository.findByIdOrNull(trendId.value)
        } returns trend

        every {
            writerTemplate.execute<Unit>(any())
        } answers {
            val callback = firstArg() as TransactionCallback<Unit>
            callback.doInTransaction(mockk())
        }

        coEvery {
            trendJpaRepository.delete(trend)
        } returns Unit

        // When
        trendService.deleteTrend(trendId)

        // Then
        coVerify { trendJpaRepository.delete(trend) }
    }
}
