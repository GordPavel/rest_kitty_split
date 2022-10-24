package ru.sau.kitty_split.event.service

import ru.sau.kitty_split.payment.service.PaymentPart
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
)

data class CreatedEvent(
    val id: UUID,
    val name: String,
    val creator: String,
    val defaultCurrency: Currency,
    val created: OffsetDateTime,
)

data class FullEvent(
    val id: UUID,
    val name: String,
    val creator: String,
    val defaultCurrency: Currency,
    val created: OffsetDateTime,
    val payments: List<EventPayment>,
)

data class EventPayment(
    val id: UUID,
    val name: String,
    val payer: String,
    val amount: BigDecimal,
    val parts: List<PaymentPart>,
    val created: OffsetDateTime,
)
