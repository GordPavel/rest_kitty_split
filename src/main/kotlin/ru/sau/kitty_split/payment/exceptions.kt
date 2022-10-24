package ru.sau.kitty_split.payment

import org.springframework.http.HttpStatus.NOT_FOUND
import ru.sau.kitty_split.commons.HttpCodeException
import java.util.UUID

class PaymentNotFoundException(
    paymentId: UUID,
    eventId: UUID,
) : HttpCodeException(
    responseStatus = NOT_FOUND,
    errorMessage = "Can't find payment $paymentId of event $eventId",
    logMessage = "Can't find payment $paymentId of event $eventId",
)
