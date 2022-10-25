package ru.sau.kitty_split.payment.dao

import java.math.BigDecimal
import java.time.OffsetDateTime
import java.util.UUID

data class CreatePaymentDto(
    val name: String,
    val payer: String,
    val spentAmounts: Map<String, BigDecimal>,
    val created: OffsetDateTime,
    val eventId: UUID,
)

data class UpdatePaymentDto(
    val id: UUID,
    val eventId: UUID,
    val name: String?,
    val payer: String?,
    val spentAmounts: Map<String, BigDecimal>?,
    val created: OffsetDateTime?,
)

data class CreatedPaymentDto(
    val id: UUID,
    val name: String,
    val payer: String,
    val spentAmounts: Map<String, BigDecimal>,
    val created: OffsetDateTime,
    val eventId: UUID,
)
