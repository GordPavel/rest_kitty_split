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
        val (_, _, _, defaultCurrency) = eventService
            .getEvent(payment.eventId)
            ?: throw EventNotFoundException(payment.eventId)

        val (amount, paymentCurrency) = payment.amount

        val targetAmount = convertPaymentAmountToEventCurrency(amount, paymentCurrency, defaultCurrency)

        return paymentsServiceMapper
            .mapCreatePaymentToCreateEntity(
                payment,
                OffsetDateTime.ofInstant(Instant.now(clock), payment.timeZone),
                targetAmount,
            )
            .let(paymentsDao::save)
            .let { paymentsServiceMapper.mapCreatedPaymentFromCreateEntity(it, defaultCurrency) }
    }

    fun updatePayment(
        payment: UpdatePayment
    ) {
        val targetAmount = payment.amount?.amount
            ?.let { sourceAmount ->
                payment.amount.currency
                    ?.let {
                        val (_, _, _, defaultCurrency) = eventService
                            .getEvent(payment.eventId)
                            ?: throw EventNotFoundException(payment.eventId)
                        convertPaymentAmountToEventCurrency(sourceAmount, payment.amount.currency, defaultCurrency)
                    }
                    ?: sourceAmount
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

}
