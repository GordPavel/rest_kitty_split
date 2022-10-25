package ru.sau.kitty_split.payment.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.sau.kitty_split.currency.service.CurrencyRatesService
import ru.sau.kitty_split.event.EventNotFoundException
import ru.sau.kitty_split.event.service.EventsService
import ru.sau.kitty_split.payment.dao.PaymentsDao
import java.math.BigDecimal
import java.time.Clock
import java.time.Instant
import java.time.OffsetDateTime
import java.util.Currency
import java.util.UUID

@Service
@Transactional
class PaymentsService(
    private val paymentsDao: PaymentsDao,
    private val paymentsServiceMapper: PaymentsServiceMapper,
    private val eventService: EventsService,
    private val currencyRatesService: CurrencyRatesService,
    private val clock: Clock,
) {

    fun createPayment(
        payment: CreatePayment,
    ): CreatedPayment {
        val (_, _, _, defaultCurrency, _, _, participants) = eventService
            .getEvent(payment.eventId)
            ?: throw EventNotFoundException(payment.eventId)

        val (_, _, _, amount, paymentCurrency, _) = payment

        val targetAmount = convertPaymentAmountToEventCurrency(amount.totalAmount(), paymentCurrency, defaultCurrency)
        val spentAmounts = amount.spentParts(participants).mapValues { (_, part) -> part * targetAmount }

        return paymentsServiceMapper
            .mapCreatePaymentToCreateEntity(
                payment,
                spentAmounts,
                OffsetDateTime.ofInstant(Instant.now(clock), payment.timeZone.normalized()),
            )
            .let(paymentsDao::save)
            .let { paymentsServiceMapper.mapCreatedPaymentFromCreateEntity(it, defaultCurrency) }
    }

    fun updatePayment(
        payment: UpdatePayment
    ) {
        val targetAmount = payment.amount
            ?.let { sourceAmount ->
                payment.currency
                    ?.let {
                        val (_, _, _, defaultCurrency, _, _, participants) = eventService
                            .getEvent(payment.eventId)
                            ?: throw EventNotFoundException(payment.eventId)
                        val targetAmount = convertPaymentAmountToEventCurrency(
                            sourceAmount.totalAmount(),
                            payment.currency,
                            defaultCurrency
                        )
                        sourceAmount.spentParts(participants).mapValues { (_, part) -> part * targetAmount }
                    }
            }

        paymentsServiceMapper
            .mapUpdatePaymentToUpdateEntity(payment, targetAmount)
            .run { paymentsDao.updatePayment(this) }
    }

    private fun convertPaymentAmountToEventCurrency(
        amount: BigDecimal,
        paymentCurrency: Currency?,
        defaultCurrency: Currency
    ): BigDecimal = paymentCurrency
        ?.let {
            if (it == defaultCurrency) amount
            else currencyRatesService.convertAmountBetweenCurrencies(amount, it, defaultCurrency)
        }
        ?: amount

    fun deletePayment(eventId: UUID, paymentId: UUID) {
        paymentsDao.deletePayment(eventId, paymentId)
    }

}
