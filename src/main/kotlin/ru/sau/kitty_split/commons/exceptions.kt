package ru.sau.kitty_split.commons

import io.swagger.annotations.ApiModel
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.time.Clock
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId

abstract class HttpCodeException(
    open val responseStatus: HttpStatus,
    open val errorMessage: String,
    open val logMessage: String? = null,
    override val cause: Throwable? = null,
) : RuntimeException(
    logMessage,
    cause,
)

@ApiModel(description = "Ошибка")
data class ApiErrorResponse(
    val errorMessage: String,
    val timestamp: OffsetDateTime,
)

@ControllerAdvice
class CommonExceptionHandler(
    private val clock: Clock,
    @Value("\${defaults.time.zone}")
    private val defaultTimeZone: ZoneId,
) : ResponseEntityExceptionHandler() {

    @ExceptionHandler(HttpCodeException::class)
    fun handleHttpErrorCodeException(
        ex: HttpCodeException,
        request: WebRequest,
    ): ResponseEntity<ApiErrorResponse> {
        val status = ex.responseStatus
        when {
            status.is5xxServerError -> logger.error(ex.logMessage, ex)
            status.is4xxClientError -> logger.warn(ex.logMessage, ex)
            else                    -> logger.trace(ex.logMessage, ex)
        }
        return ResponseEntity
            .status(status)
            .body(
                ApiErrorResponse(
                    timestamp = OffsetDateTime.ofInstant(Instant.now(clock), defaultTimeZone),
                    errorMessage = ex.errorMessage,
                )
            )
    }

    @ExceptionHandler(Throwable::class)
    fun handleUnknownException(
        ex: Throwable,
        request: WebRequest,
    ): ResponseEntity<ApiErrorResponse> {
        logger.error(ex.message, ex)
        return ResponseEntity
            .status(INTERNAL_SERVER_ERROR)
            .body(
                ApiErrorResponse(
                    timestamp = OffsetDateTime.ofInstant(Instant.now(clock), defaultTimeZone),
                    errorMessage = "Unexcpected error",
                )
            )
    }

}
