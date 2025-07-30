package com.goofy.realtime.outbound.rest

import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.core.env.Environment
import org.springframework.http.HttpStatus
import java.time.LocalDateTime

class HealthResourceTest {
    private lateinit var healthResource: HealthResource
    private lateinit var environment: Environment

    @BeforeEach
    fun setUp() {
        environment = mockk()
        healthResource = HealthResource(environment)
    }

    @Test
    fun `healthCheckV1 should return health response with OK status`() = runTest {
        // Given
        val activeProfile = "test"
        val now = LocalDateTime.now()

        every {
            environment.activeProfiles
        } returns arrayOf(activeProfile)

        // When
        val result = healthResource.healthCheckV1()

        // Then
        result.statusCode shouldBe HttpStatus.OK

        val healthResponse = result.body?.data
        healthResponse?.env shouldBe activeProfile
        healthResponse?.message shouldBe "Health Good!"
    }
}
