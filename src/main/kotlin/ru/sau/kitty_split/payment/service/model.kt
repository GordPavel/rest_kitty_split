package ru.sau.kitty_split.payment.service

import java.math.BigDecimal
import java.time.OffsetDateTime
import java.time.ZoneId
import java.util.Currency
import java.util.UUID

data class CreatePayment(
    val name: String,
    val payer: String,
    val amount: CreatePaymentAmount,
    val parts: List<PaymentPart>,
    val eventId: UUID,
    val timeZone: ZoneId,
)

data class UpdatePayment(
    val id: UUID,
    val eventId: UUID,
    val name: String?,
    val payer: String?,
    val amount: UpdatePaymentAmount?,
    val parts: List<PaymentPart>?,
    val created: OffsetDateTime?,
)

data class CreatePaymentAmount(
    val amount: BigDecimal,
    val currency: Currency?,
)

data class UpdatePaymentAmount(
    val amount: BigDecimal?,
    val currency: Currency?,
)

data class CreatedPayment(
    val id: UUID,
    val name: String,
    val payer: String,
    val amount: CreatedPaymentAmount,
    val parts: List<PaymentPart>,
    val created: OffsetDateTime,
    val eventId: UUID,
)

data class CreatedPaymentAmount(
    val amount: BigDecimal,
    val currency: Currency?,
)

data class PaymentPart(
    val payee: String,
    val part: BigDecimal,
)
