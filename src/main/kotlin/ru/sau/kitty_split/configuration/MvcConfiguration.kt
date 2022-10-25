package ru.sau.kitty_split.configuration

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.math.BigDecimal
import java.math.RoundingMode.HALF_UP
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

@Configuration
@EnableWebMvc
@EnableConfigurationProperties(
    ExternalRestConfigurations::class,
)
class MvcConfiguration(
    private val externalRestConfigurations: ExternalRestConfigurations,
) : WebMvcConfigurer {
    @Bean
    @Primary
    fun externalRestJacksonObjectMapper(): ObjectMapper = ObjectMapper()
        .registerKotlinModule()
        .registerModule(RestJavaTimeModule(externalRestConfigurations.format))
        .registerModule(RestBigDecimalModule(externalRestConfigurations.format))

    override fun configureMessageConverters(converters: MutableList<HttpMessageConverter<*>>) {
        converters.add(MappingJackson2HttpMessageConverter(externalRestJacksonObjectMapper()))
    }

}

@ConfigurationProperties("spring.mvc")
@ConstructorBinding
data class ExternalRestConfigurations(
    val format: ExternalRestFormatConfigurations,
)

@ConstructorBinding
data class ExternalRestFormatConfigurations(
    val offsetDateTime: String,
    val bigDecimalScale: Int,
)

private class RestJavaTimeModule(
    externalRestFormatConfigurations: ExternalRestFormatConfigurations,
) : SimpleModule() {
    init {
        val restOffsetDateTimeFormatter = DateTimeFormatter.ofPattern(externalRestFormatConfigurations.offsetDateTime)
        addSerializer(object : StdSerializer<OffsetDateTime>(OffsetDateTime::class.java) {
            override fun serialize(value: OffsetDateTime, gen: JsonGenerator, provider: SerializerProvider) {
                gen.writeString(restOffsetDateTimeFormatter.format(value))
            }
        })
        addDeserializer(
            OffsetDateTime::class.java,
            object : StdDeserializer<OffsetDateTime>(OffsetDateTime::class.java) {
                override fun deserialize(p: JsonParser, ctxt: DeserializationContext): OffsetDateTime =
                    OffsetDateTime.parse(p.valueAsString, restOffsetDateTimeFormatter)
            }
        )
    }
}

private class RestBigDecimalModule(
    externalRestFormatConfigurations: ExternalRestFormatConfigurations,
) : SimpleModule() {
    init {
        addSerializer(
            BigDecimal::class.java,
            object : StdSerializer<BigDecimal>(BigDecimal::class.java) {
                override fun serialize(value: BigDecimal, gen: JsonGenerator, provider: SerializerProvider) {
                    gen.writeString(
                        value.setScale(externalRestFormatConfigurations.bigDecimalScale, HALF_UP).toString()
                    )
                }
            }
        )
    }
}
