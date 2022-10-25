package ru.sau.kitty_split.split.service

import org.springframework.stereotype.Service
import ru.sau.kitty_split.event.service.EventPayment
import java.math.BigDecimal
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

    fun mapPaymentsToInputData(payments: List<EventPayment>, participants: List<String>): InputData {
        val inputDataPayments = payments
            .asSequence()
            .map { payment ->
                Spending(
                    payment.payer,
                    payment.name,
                    participants.map { payment.spentAmounts[it] ?: BigDecimal.ZERO },
                )
            }
            .asIterable()

        return InputData(participants, inputDataPayments)
    }
}
