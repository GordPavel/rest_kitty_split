package ru.sau.kitty_split.split.service

import java.math.BigDecimal
import java.util.Currency

data class InputData(
    val participants: List<String>,
    val spendings: Iterable<Spending>,
)

data class OutputData(
    val participants: List<String>,
    val transactions: Map<String, Map<String, BigDecimal>>,
)

data class Spending(
    val payer: String,
    val good: String,
    val parts: List<BigDecimal>,
)

data class Transaction(
    val payer: String,
    val payee: String,
    val amount: BigDecimal,
    val currency: Currency,
)
