package ru.sau.kitty_split.event.service

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
