package ru.sau.kitty_split

import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
class KittySplitApplicationTests {

    private lateinit var application: KittySplitApplication

    @Test
    fun contextLoads() {
        assertNotNull(application)
    }

}
