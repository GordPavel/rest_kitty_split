package ru.sau.kitty_split.payment.service

import java.math.BigDecimal
import java.math.RoundingMode.HALF_UP
import java.time.OffsetDateTime
import java.time.ZoneId
import java.util.Currency
import java.util.UUID

data class CreatePayment(
    val eventId: UUID,
    val name: String,
    val payer: String,
    val amount: PaymentAmount,
    val currency: Currency?,
    val timeZone: ZoneId,
)

data class CreatedPayment(
    val id: UUID,
    val eventId: UUID,
    val name: String,
    val payer: String,
    val amount: PaymentAmountsAmount,
    val currency: Currency?,
    val created: OffsetDateTime,
)

data class UpdatePayment(
    val id: UUID,
    val eventId: UUID,
    val name: String?,
    val payer: String?,
    val amount: PaymentAmount?,
    val currency: Currency?,
    val created: OffsetDateTime?,
)

sealed interface PaymentAmount {
    fun totalAmount(): BigDecimal
    fun spentAmounts(participants: List<String>): Map<String, BigDecimal>

    fun spentParts(participants: List<String>): Map<String, BigDecimal>
}

data class PaymentEqualAmount(
    val totalAmount: BigDecimal,
) : PaymentAmount {
    override fun totalAmount(): BigDecimal = totalAmount
    override fun spentAmounts(participants: List<String>): Map<String, BigDecimal> {
        val part = BigDecimal.ONE.divide(BigDecimal(participants.size), 100, HALF_UP)
        return participants.associateWith { totalAmount() * part }
    }

    override fun spentParts(participants: List<String>): Map<String, BigDecimal> {
        val part = BigDecimal.ONE.divide(BigDecimal(participants.size), 100, HALF_UP)
        return participants.associateWith { part }
    }
}

data class PaymentPartsAmount(
    val totalAmount: BigDecimal,
    val spentParts: Map<String, BigDecimal>,
) : PaymentAmount {
    override fun totalAmount(): BigDecimal = totalAmount
    override fun spentAmounts(participants: List<String>): Map<String, BigDecimal> =
        spentParts(participants).mapValues { (_, part) -> totalAmount * part }

    override fun spentParts(participants: List<String>): Map<String, BigDecimal> {
        val totalParts = spentParts.values.reduce(BigDecimal::add)
        return spentParts.mapValues { (_, part) -> part.divide(totalParts, 100, HALF_UP) }
    }
}

data class PaymentAmountsAmount(
    val spentAmounts: Map<String, BigDecimal>,
) : PaymentAmount {
    override fun totalAmount(): BigDecimal = spentAmounts.values.reduce(BigDecimal::add)
    override fun spentAmounts(participants: List<String>): Map<String, BigDecimal> = spentAmounts

    override fun spentParts(participants: List<String>): Map<String, BigDecimal> {
        val totalAmount = totalAmount()
        return spentAmounts.mapValues { (_, amount) -> amount.divide(totalAmount, 100, HALF_UP) }
    }
}
