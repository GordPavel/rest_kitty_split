package ru.sau.kitty_split.util

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import java.time.Clock
import java.time.Instant
import java.time.ZoneId

@TestConfiguration
class FixedClockTestConfiguration {
    @Bean
    fun clock(
        @Value("\${defaults.time.zone}") defaultZoneId: ZoneId
    ): Clock = Clock.fixed(Instant.parse("2022-01-01T00:00:00.000000Z"), defaultZoneId)
}