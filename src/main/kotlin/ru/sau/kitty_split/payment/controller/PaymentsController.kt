package ru.sau.kitty_split.payment.controller

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

    @PostMapping
    @ResponseStatus(CREATED)
    fun creatPayment(
        @PathVariable eventId: UUID,
        @RequestBody payment: CreatePaymentControllerDto,
        @RequestHeader(value = "timezone", required = false) headerTimezone: String?,
    ): CreatedPaymentControllerDto = paymentsControllerMapper
        .fromControllerDto(
            payment,
            eventId,
            parseTimeZone(headerTimezone, defaultTimeZone),
        )
        .let(paymentsService::createPayment)
        .let(paymentsControllerMapper::toControllerDto)

    @PatchMapping("/{paymentId}")
    @ResponseStatus(OK)
    fun updatePayment(
        @PathVariable eventId: UUID,
        @PathVariable paymentId: UUID,
        @RequestBody paymentUpdate: UpdatePaymentControllerDto
    ) {
        paymentsControllerMapper
            .fromControllerDto(paymentUpdate, paymentId, eventId)
            .run { paymentsService.updatePayment(this) }
    }

    @DeleteMapping("/{paymentId}")
    @ResponseStatus(OK)
    fun removePayment(
        @PathVariable eventId: UUID,
        @PathVariable paymentId: UUID,
    ) {
        paymentsService.deletePayment(eventId, paymentId)
    }

}