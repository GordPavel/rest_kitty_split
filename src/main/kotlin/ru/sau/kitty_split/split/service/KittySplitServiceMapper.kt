package ru.sau.kitty_split.split.service

import org.springframework.stereotype.Service
import ru.sau.kitty_split.event.service.EventPayment
import java.math.BigDecimal
import java.math.RoundingMode.HALF_UP
import java.util.Currency

@Service
class KittySplitServiceMapper {
    fun mapOutputDataToTransactions(
        transactions: Map<String, Map<String, BigDecimal>>,
        currency: Currency,
    ): List<Transaction> = transactions
        .flatMap { (payer, payments) ->
            payments.map { (payee, amount) -> Transaction(payer, payee, amount, currency) }
        }
        .filter { it.amount > BigDecimal(0.01) }

    fun mapPaymentsToInputData(payments: List<EventPayment>): InputData {
        val allPayers = payments.asSequence()
            .map { it.payer }
        val allPayees = payments.asSequence()
            .flatMap { payment -> payment.parts.map { part -> part.payee } }
        val participants = (allPayers + allPayees).toSortedSet().toList()

        val inputDataPayments = payments
            .asSequence()
            .map { payment ->
                val partsSum = payment.parts.map { it.part }.reduce(BigDecimal::add)
                val parts = participants
                    .map { participant ->
                        payment.parts
                            .find { it.payee == participant }
                            ?.let { (_, part) -> (part.divide(partsSum, 100, HALF_UP)) * payment.amount }
                            ?: BigDecimal.ZERO
                    }
                Spending(
                    payment.payer,
                    payment.name,
                    parts,
                )
            }
            .asIterable()

        return InputData(participants, inputDataPayments)
    }
}
