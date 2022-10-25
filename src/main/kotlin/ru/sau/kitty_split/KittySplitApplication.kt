package ru.sau.kitty_split

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(
    exclude = [
        HttpMessageConvertersAutoConfiguration::class,
    ]
)
class KittySplitApplication

fun main(args: Array<String>) {
    runApplication<KittySplitApplication>(*args)
}
