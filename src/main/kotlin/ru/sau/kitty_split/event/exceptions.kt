package ru.sau.kitty_split.event

import org.springframework.http.HttpStatus.NOT_FOUND
import ru.sau.kitty_split.commons.HttpCodeException
import java.util.UUID

class EventNotFoundException(
    eventId: UUID,
) : HttpCodeException(
    responseStatus = NOT_FOUND,
    errorMessage = "Can't find event $eventId",
    logMessage = "Can't find event $eventId",
)
