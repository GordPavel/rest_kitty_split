package ru.sau.kitty_split.payment.controller

import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.OK
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import ru.sau.kitty_split.commons.ApiErrorResponse
import ru.sau.kitty_split.commons.parseTimeZone
import ru.sau.kitty_split.payment.service.PaymentsService
import java.time.ZoneId
import java.util.UUID

@RestController
@RequestMapping("/events/{eventId}/payments")
@Validated
class PaymentsController(
    @Value("\${defaults.time.zone}")
    private val defaultTimeZone: ZoneId,
    private val paymentsService: PaymentsService,
    private val paymentsControllerMapper: PaymentsControllerMapper,
) {

    @ApiOperation(value = "Создание нового платежа")
    @ApiResponses(
        ApiResponse(code = 201, response = CreatedPaymentControllerDto::class, message = "Платеж успешно создан"),
        ApiResponse(code = 400, response = ApiErrorResponse::class, message = "Ошибка входных данных"),
        ApiResponse(code = 500, response = ApiErrorResponse::class, message = "Внутренняя ошибка сервера"),
    )
    @PostMapping
    @ResponseStatus(CREATED)
    fun creatPayment(
        @ApiParam(
            name = "Id события",
            example = "576ac17b-c5b9-4bb7-a33f-317e38d03044",
            required = true,
            type = "java.util.UUID",
        )
        @PathVariable eventId: UUID,
        @RequestBody payment: CreatePaymentControllerDto,
        @ApiParam(
            name = "Зона часового пояса",
            type = "java.time.ZoneId",
            defaultValue = "Europe/Moscow",
            required = false,
        )
        @RequestHeader(value = "timezone", required = false) headerTimezone: String?,
    ): CreatedPaymentControllerDto = paymentsControllerMapper
        .fromControllerDto(
            payment,
            eventId,
            parseTimeZone(headerTimezone, defaultTimeZone),
        )
        .let(paymentsService::createPayment)
        .let(paymentsControllerMapper::toControllerDto)

    @ApiOperation(value = "Частичное обновление платежа")
    @ApiResponses(
        ApiResponse(code = 200, message = "Платеж успешно обновлен"),
        ApiResponse(code = 400, response = ApiErrorResponse::class, message = "Ошибка входных данных"),
        ApiResponse(code = 404, response = ApiErrorResponse::class, message = "Сущность не найдена"),
        ApiResponse(code = 500, response = ApiErrorResponse::class, message = "Внутренняя ошибка сервера"),
    )
    @PatchMapping("/{paymentId}")
    @ResponseStatus(OK)
    fun updatePayment(
        @ApiParam(
            name = "Id события",
            example = "576ac17b-c5b9-4bb7-a33f-317e38d03044",
            required = true,
            type = "java.util.UUID",
        )
        @PathVariable eventId: UUID,
        @ApiParam(
            name = "Id платежа",
            example = "576ac17b-c5b9-4bb7-a33f-317e38d03044",
            required = true,
            type = "java.util.UUID",
        )
        @PathVariable paymentId: UUID,
        @RequestBody paymentUpdate: UpdatePaymentControllerDto
    ) {
        paymentsControllerMapper
            .fromControllerDto(paymentUpdate, paymentId, eventId)
            .run { paymentsService.updatePayment(this) }
    }

    @ApiOperation(value = "Удаление платежа")
    @ApiResponses(
        ApiResponse(code = 200, message = "Платеж успешно удален"),
        ApiResponse(code = 400, response = ApiErrorResponse::class, message = "Ошибка входных данных"),
        ApiResponse(code = 500, response = ApiErrorResponse::class, message = "Внутренняя ошибка сервера"),
    )
    @DeleteMapping("/{paymentId}")
    @ResponseStatus(OK)
    fun removePayment(
        @ApiParam(
            name = "Id события",
            example = "576ac17b-c5b9-4bb7-a33f-317e38d03044",
            required = true,
            type = "java.util.UUID",
        )
        @PathVariable eventId: UUID,
        @ApiParam(
            name = "Id платежа",
            example = "576ac17b-c5b9-4bb7-a33f-317e38d03044",
            required = true,
            type = "java.util.UUID",
        )
        @PathVariable paymentId: UUID,
    ) {
        paymentsService.deletePayment(eventId, paymentId)
    }

}