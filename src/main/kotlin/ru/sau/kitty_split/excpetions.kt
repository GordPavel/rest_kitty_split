package ru.sau.kitty_split

import org.springframework.http.HttpStatus.BAD_REQUEST
import ru.sau.kitty_split.commons.HttpCodeException

class IncorrectCurrencyCodeException(
    currencyCode: String,
    cause: Throwable? = null,
) : HttpCodeException(
    responseStatus = BAD_REQUEST,
    errorMessage = "Can't recognize currency $currencyCode",
    logMessage = "Can't recognize currency $currencyCode",
    cause = cause,
)

class InvalidTimeZoneException(
    incorrectTimeZone: String,
    override val cause: Throwable,
) : HttpCodeException(
    responseStatus = BAD_REQUEST,
    errorMessage = "Incorrect time zone $incorrectTimeZone",
    logMessage = "Can't parse incorrect time zone $incorrectTimeZone",
    cause = cause,
)
