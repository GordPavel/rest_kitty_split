package ru.sau.kitty_split.event.dao

import java.time.OffsetDateTime
import java.util.Currency

data class CreateEventDto(
    val name: String,
    val creator: String,
    val created: OffsetDateTime,
    val defaultCurrency: Currency,
)
