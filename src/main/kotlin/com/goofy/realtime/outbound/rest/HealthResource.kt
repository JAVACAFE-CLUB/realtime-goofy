package com.goofy.realtime.outbound.rest

import com.goofy.realtime.common.extension.wrapOk
import com.goofy.realtime.common.model.Response
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.core.env.Environment
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@Tag(name = "health check")
@RestController
@RequestMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
class HealthResource(
    private val environment: Environment,
) {
    @GetMapping("/api/v1/health")
    @ResponseStatus(HttpStatus.OK)
    suspend fun healthCheckV1(): ResponseEntity<Response<HealthResponse>> {
        return environment.activeProfiles.first()
            .run { HealthResponse.from(this) }
            .wrapOk()
    }
}

data class HealthResponse(
    val env: String,
    val dateTime: LocalDateTime = LocalDateTime.now(),
    val message: String = "Health Good!"
) {
    companion object {
        fun from(env: String): HealthResponse {
            return HealthResponse(env = env)
        }
    }
}
