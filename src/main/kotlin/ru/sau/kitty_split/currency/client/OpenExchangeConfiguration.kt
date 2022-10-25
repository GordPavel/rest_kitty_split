package ru.sau.kitty_split.currency.client

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import mu.KotlinLogging
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.client.ClientHttpResponse
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.ResponseErrorHandler
import org.springframework.web.client.RestTemplate
import java.math.BigDecimal
import java.util.Currency

private val log = KotlinLogging.logger { }

@Configuration
@EnableConfigurationProperties(
    CurrencyRatesProperties::class,
)
class OpenExchangeConfiguration {

    @Bean
    fun openExchangeRestTemplate(
        currencyRatesProperties: CurrencyRatesProperties,
        builder: RestTemplateBuilder,
    ): RestTemplate {
        val mapper = openExchangeRestObjectMapper()
        return builder
            .messageConverters(
                MappingJackson2HttpMessageConverter(mapper),
            )
            .errorHandler(OpenExchangeResponseErrorHandler(mapper))
            .defaultHeader(AUTHORIZATION, "Token ${currencyRatesProperties.appId}")
            .rootUri(currencyRatesProperties.baseUrl)
            .build()
    }

    @Bean
    fun openExchangeRestObjectMapper(): ObjectMapper = ObjectMapper()
        .registerKotlinModule()
        .registerModule(OpenExchangeResponseModule)
        .configure(FAIL_ON_UNKNOWN_PROPERTIES, false)

}

@ConfigurationProperties("external.open.exchange")
@ConstructorBinding
data class CurrencyRatesProperties(
    val baseUrl: String,
    val appId: String,
)

private class OpenExchangeResponseErrorHandler(
    private val mapper: ObjectMapper,
) : ResponseErrorHandler {
    override fun hasError(response: ClientHttpResponse): Boolean = !response.statusCode.is2xxSuccessful

    override fun handleError(response: ClientHttpResponse) {
        val (message, description) = mapper.readValue(
            response.body,
            GetCurrencyRatesFailedResponseDto::class.java
        )
        throw OpenExchangeException(
            logMessage = "Error calling currencies service: $message, $description",
        )
    }
}

private object OpenExchangeResponseModule : SimpleModule() {

    private val mapper = ObjectMapper()

    init {
        addDeserializer(
            Map::class.java,
            object : StdDeserializer<Map<Currency, BigDecimal>>(Map::class.java) {
                override fun deserialize(
                    p: JsonParser,
                    ctxt: DeserializationContext
                ): Map<Currency, BigDecimal> {
                    val codec = p.codec
                    val tree = codec.readTree<JsonNode>(p)
                    return mapper
                        .readValue<Map<String, BigDecimal>>(tree.toString())
                        .asSequence()
                        .flatMap {
                            try {
                                sequenceOf(
                                    Currency.getInstance(it.key) to it.value
                                )
                            } catch (e: IllegalArgumentException) {
                                log.warn { "Can't parse currency ${it.key}" }
                                sequenceOf()
                            }
                        }
                        .toMap()
                }
            })
    }

}
