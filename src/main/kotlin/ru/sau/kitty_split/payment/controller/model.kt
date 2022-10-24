package ru.sau.kitty_split.payment.controller

import java.math.BigDecimal
import java.time.OffsetDateTime
import java.util.UUID
import javax.validation.constraints.NotNull
import javax.validation.constraints.Null

data class CreatePaymentControllerDto(
    @Null
    val id: UUID?,
    @NotNull
    val name: String,
    @NotNull
    val payer: String,
    val amount: PaymentAmountDto,
    val parts: List<PaymentPartDto> = emptyList(),
)

data class CreatedPaymentControllerDto(
    val id: UUID,
    val name: String,
    val payer: String,
    val amount: PaymentAmountDto,
    val parts: List<PaymentPartDto>,
    val created: OffsetDateTime,
    val eventId: UUID,
)

data class UpdatePaymentControllerDto(
    val name: String?,
    val payer: String?,
    val amount: PaymentAmountDto?,
    val parts: List<PaymentPartDto>?,
    val created: OffsetDateTime?,
)

data class PaymentAmountDto(
    @NotNull
    val amount: BigDecimal,
    val currency: String?,
)

data class PaymentPartDto(
    @NotNull
    val payee: String,
    @NotNull
    val part: BigDecimal,
)
