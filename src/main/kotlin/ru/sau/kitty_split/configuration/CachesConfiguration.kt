package ru.sau.kitty_split.configuration

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import com.github.benmanes.caffeine.cache.Expiry
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.caffeine.CaffeineCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Clock
import java.time.Duration
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Configuration
@EnableCaching
class CachesConfiguration(
    private val clock: Clock,
) {

    @Bean
    fun caffeineCacheManager(): CacheManager = CaffeineCacheManager().apply {
        registerCustomCache("currency_rates", currencyRatesCache())
    }

    private fun currencyRatesCache(): Cache<Any, Any> =
        Caffeine.newBuilder()
            .expireAfter(object : Expiry<Any, Any> {
                override fun expireAfterCreate(key: Any, value: Any, currentTime: Long): Long {
                    val currentTime = LocalDateTime.now(clock)
                    val targetExpirationTime = currentTime
                        .truncatedTo(ChronoUnit.HOURS)
                        .plus(Duration.parse("PT1H"))

                    return Duration.between(currentTime, targetExpirationTime).toNanos()
                }

                override fun expireAfterUpdate(
                    key: Any, value: Any,
                    currentTime: Long, currentDuration: Long
                ): Long = currentDuration

                override fun expireAfterRead(
                    key: Any, value: Any,
                    currentTime: Long, currentDuration: Long
                ): Long = currentDuration
            })
            .build()

}