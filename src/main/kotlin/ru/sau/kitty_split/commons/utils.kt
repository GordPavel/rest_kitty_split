package ru.sau.kitty_split.commons

import ru.sau.kitty_split.event.controller.InvalidTimeZoneException
import java.time.DateTimeException
import java.time.ZoneId

fun parseTimeZone(headerTimezone: String?, defaultTimeZone: ZoneId) = try {
    headerTimezone?.let(ZoneId::of) ?: defaultTimeZone
} catch (e: DateTimeException) {
    throw InvalidTimeZoneException(headerTimezone!!, e)
}
