package com.goofy.realtime.exception.advice

import com.goofy.realtime.common.model.ErrorResponse
import com.goofy.realtime.exception.RealtimeException
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class RealtimeExceptionHandler {
    private val logger = KotlinLogging.logger { }

    @ExceptionHandler(RealtimeException::class)
    protected fun handleRealtimeException(e: RealtimeException): ResponseEntity<ErrorResponse> {
        logger.warn { "Realtime¬Exception ${e.message}" }
        val response = ErrorResponse(
            errorCode = e.errorCode.name,
            reason = e.message ?: e.errorCode.description,
            extra = e.extra
        )
        return ResponseEntity(response, e.errorCode.status)
    }
}
