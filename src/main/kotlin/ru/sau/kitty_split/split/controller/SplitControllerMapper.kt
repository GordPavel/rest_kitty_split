package ru.sau.kitty_split.split.controller

import org.springframework.stereotype.Service
import ru.sau.kitty_split.split.service.Transaction

@Service
class SplitControllerMapper {
    fun mapTransactions(transactions: List<Transaction>): List<TransactionDto> = transactions
        .map { TransactionDto(it.payer, it.payee, TransactionAmount(it.amount, it.currency)) }
}
