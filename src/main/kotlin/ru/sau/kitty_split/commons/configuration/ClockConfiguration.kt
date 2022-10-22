package ru.sau.kitty_split.commons.configuration

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Clock
import java.time.ZoneId

@Configuration
class ClockConfiguration(
    @Value("\${defaults.time.zone}")
    private val defaultTimeZone: ZoneId,
) {
    @Bean
    fun clock(): Clock = Clock.system(defaultTimeZone)
}
