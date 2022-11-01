package ru.sau.kitty_split.event.service

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import ru.sau.kitty_split.split.service.InputData
import ru.sau.kitty_split.split.service.Spending
import ru.sau.kitty_split.split.service.SplitService
import ru.sau.kitty_split.split.service.SplitServiceStub
import java.math.BigDecimal

@Disabled
internal class SplitServiceTest {

    private val splitService: SplitService = SplitServiceStub()

    @Test
    fun `given TwoSimplePayment when split payment then split is correct`() {
        val data = InputData(
            listOf("Даша", "Паша", "Вася"),
            listOf(
                Spending(
                    "Даша",
                    "Овощи",
                    listOf(
                        BigDecimal(0),
                        BigDecimal(500),
                        BigDecimal(0),
                    )
                ),
                Spending(
                    "Паша",
                    "Фрукты",
                    listOf(
                        BigDecimal(0),
                        BigDecimal(0),
                        BigDecimal(500),
                    )
                ),
            ),
        )

        val (_, transactions: Map<String, Map<String, BigDecimal>>) = splitService.splitBill(data)

        Assertions.assertEquals(
            mapOf(
                "Вася" to mapOf("Даша" to BigDecimal(500))
            ),
            transactions,
        )
    }

}