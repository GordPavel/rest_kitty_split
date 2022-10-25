package ru.sau.kitty_split.currency.client

import java.math.BigDecimal
import java.util.Currency

data class GetCurrencyRatesRequestDto(
    val baseCurrency: Currency = Currency.getInstance("USD"),
)

data class GetCurrencyRatesSuccessfulResponseDto(
    val rates: Map<Currency, BigDecimal>
)

data class GetCurrencyRatesFailedResponseDto(
    val message: String?,
    val description: String?,
)
