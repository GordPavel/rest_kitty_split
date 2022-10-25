package ru.sau.kitty_split.event.service

import java.math.BigDecimal
import java.time.OffsetDateTime
import java.time.ZoneId
import java.util.Currency
import java.util.UUID

data class CreateEvent(
    val name: String,
    val creator: String,
    val defaultCurrency: Currency,
    val timeZone: ZoneId,
    val participants: List<String>,
)

data class CreatedEvent(
    val id: UUID,
    val name: String,
    val creator: String,
    val defaultCurrency: Currency,
    val created: OffsetDateTime,
    val participants: List<String>,
)

data class FullEvent(
    val id: UUID,
    val name: String,
    val creator: String,
    val defaultCurrency: Currency,
    val created: OffsetDateTime,
    val payments: List<EventPayment>,
    val participants: List<String>,
)

data class EventPayment(
    val id: UUID,
    val name: String,
    val payer: String,
    val spentAmounts: Map<String, BigDecimal>,
    val created: OffsetDateTime,
)
