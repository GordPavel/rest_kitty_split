package ru.sau.kitty_split.split.controller

import java.math.BigDecimal
import java.util.Currency

data class TransactionDto(
    val payer: String,
    val payee: String,
    val amount: TransactionAmount,
)

data class TransactionAmount(
    val amount: BigDecimal,
    val currency: Currency,
)
