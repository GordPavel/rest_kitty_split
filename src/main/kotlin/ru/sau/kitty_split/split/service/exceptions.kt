package ru.sau.kitty_split.split.service

import org.springframework.http.HttpStatus.BAD_REQUEST
import ru.sau.kitty_split.commons.HttpCodeException

abstract class SplitServiceException(
    message: String,
) : HttpCodeException(
    responseStatus = BAD_REQUEST,
    errorMessage = message,
    logMessage = message
)

class IncorrectPayerSplitServiceException(
    payment: String,
    payer: String,
) : SplitServiceException("За товар %s расплатился неизвестный участник %s".format(payment, payer))

class IncorrectPaymentSplitServiceException(
    payment: String,
    payer: String? = null,
) : SplitServiceException("Ошибка в трате %s".format(payment, payer?.let { " с участником $it" } ?: ""))
