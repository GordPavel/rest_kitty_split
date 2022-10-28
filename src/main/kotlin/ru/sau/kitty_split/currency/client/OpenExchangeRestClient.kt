package ru.sau.kitty_split.currency.client

import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import ru.sau.kitty_split.currency.OpenExchangeException
import java.math.BigDecimal
import java.util.Currency

@Service
class OpenExchangeRestClient(
    private val openExchangeRestTemplate: RestTemplate,
) {
    @Cacheable(cacheNames = ["currency_rates"], key = "#request.baseCurrency.currencyCode")
    fun getCurrencyRates(
        request: GetCurrencyRatesRequestDto,
    ): Map<Currency, BigDecimal> {
        val response: GetCurrencyRatesSuccessfulResponseDto = openExchangeRestTemplate
            .getForEntity(
                "/latest.json",
                GetCurrencyRatesSuccessfulResponseDto::class.java,
                mapOf(
                    "base" to request.baseCurrency.currencyCode
                )
            )
            .body
            ?: throw OpenExchangeException(
                logMessage = "Unknown error calling currencies service",
            )
        return response.rates
    }

}
