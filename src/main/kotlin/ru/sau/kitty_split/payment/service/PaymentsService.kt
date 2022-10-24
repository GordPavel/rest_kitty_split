package ru.sau.kitty_split.payment.service

import org.springframework.stereotype.Service
import ru.sau.kitty_split.event.controller.EventNotFoundException
import ru.sau.kitty_split.event.service.EventsService
import ru.sau.kitty_split.payment.dao.PaymentsDao
import java.time.Clock
import java.time.Instant
import java.time.OffsetDateTime

@Service
class PaymentsService(
    private val paymentsDao: PaymentsDao,
    private val paymentsServiceMapper: PaymentsServiceMapper,
    private val eventService: EventsService,
    private val clock: Clock,
) {

    fun createPayment(
        payment: CreatePayment,
    ): CreatedPayment {
        val (_, _, _, defaultCurrency) = eventService
            .getEvent(payment.eventId)
            ?: throw EventNotFoundException(payment.eventId)

        // todo @gordeevp Convert amount to default currency
        val (amount, _) = payment.amount

        return paymentsServiceMapper
            .mapCreatePaymentToCreateEntity(
                payment,
                OffsetDateTime.ofInstant(Instant.now(clock), payment.timeZone),
                amount,
            )
            .let(paymentsDao::save)
            .let { paymentsServiceMapper.mapCreatedPaymentFromCreateEntity(it, defaultCurrency) }
    }

}
