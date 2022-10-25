package ru.sau.kitty_split.currency.client

import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import ru.sau.kitty_split.commons.HttpCodeException

class OpenExchangeException(
    override val logMessage: String? = null,
    override val cause: Throwable? = null,
) : HttpCodeException(
    responseStatus = INTERNAL_SERVER_ERROR,
    "Sorry, currency rates are currently unavailable, " +
            "please specify payment amount the same as event default or don't specify in request",
    logMessage,
    cause,
)
