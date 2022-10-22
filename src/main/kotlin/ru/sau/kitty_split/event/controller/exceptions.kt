package ru.sau.kitty_split.event.controller

import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.NOT_FOUND
import ru.sau.kitty_split.commons.HttpCodeException
import java.util.UUID

class InvalidTimeZoneException(
    incorrectTimeZone: String,
    override val cause: Throwable,
) : HttpCodeException(
    responseStatus = BAD_REQUEST,
    errorMessage = "Incorrect time zone $incorrectTimeZone",
    logMessage = "Can't parse incorrect time zone $incorrectTimeZone",
    cause = cause,
)

class EventNotFoundException(
    eventId: UUID,
) : HttpCodeException(
    responseStatus = NOT_FOUND,
    errorMessage = "Can't find event $eventId",
    logMessage = "Can't find event $eventId",
)
