package ru.sau.kitty_split.currency.service

import org.springframework.stereotype.Service
import ru.sau.kitty_split.IncorrectCurrencyCodeException
import ru.sau.kitty_split.currency.client.GetCurrencyRatesRequestDto
import ru.sau.kitty_split.currency.client.OpenExchangeRestClient
import java.math.BigDecimal
import java.math.RoundingMode.HALF_UP
import java.util.Currency

@Service
class CurrencyRatesService(
    private val openExchangeRestClient: OpenExchangeRestClient,
) {

    fun convertAmountBetweenCurrencies(
        sourceAmount: BigDecimal,
        sourceCurrency: Currency,
        targetCurrency: Currency,
    ): BigDecimal {
        val currencyRates = openExchangeRestClient.getCurrencyRates(GetCurrencyRatesRequestDto())
        val sourceRate = currencyRates[sourceCurrency]
            ?: throw IncorrectCurrencyCodeException(sourceCurrency.currencyCode)
        val targetRate = currencyRates[targetCurrency]
            ?: throw IncorrectCurrencyCodeException(targetCurrency.currencyCode)

        return sourceAmount * (targetRate.divide(sourceRate, 100, HALF_UP))
    }

}

