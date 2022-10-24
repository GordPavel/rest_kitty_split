package ru.sau.kitty_split.payment.dao

import ru.sau.kitty_split.payment.service.PaymentPart
import java.math.BigDecimal
import java.time.OffsetDateTime
import java.util.UUID

data class CreatePaymentDto(
    val name: String,
    val payer: String,
    val amount: BigDecimal,
    val parts: List<PaymentPart> = emptyList(),
    val created: OffsetDateTime,
    val eventId: UUID,
)

data class CreatedPaymentDto(
    val id: UUID,
    val name: String,
    val payer: String,
    val amount: BigDecimal,
    val parts: List<PaymentPart> = emptyList(),
    val created: OffsetDateTime,
    val eventId: UUID,
)
