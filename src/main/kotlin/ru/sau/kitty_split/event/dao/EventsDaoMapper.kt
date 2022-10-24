package ru.sau.kitty_split.event.dao

import org.springframework.stereotype.Service
import ru.sau.kitty_split.event.service.CreatedEvent
import ru.sau.kitty_split.event.service.EventPayment
import ru.sau.kitty_split.event.service.FullEvent
import ru.sau.kitty_split.payment.service.PaymentPart
import ru.sau.kitty_split.util.SqlTimestampMapper
import java.math.BigDecimal
import java.util.Currency

@Service
class EventsDaoMapper(
    private val sqlTimestampMapper: SqlTimestampMapper,
) {

    fun mapEventForCreation(event: CreateEventDto): EventEntity {
        val (timestamp, offset) = sqlTimestampMapper.mapOffsetDateTimeToSqlTimeStamp(event.created)
        return EventEntity(
            null,
            event.name,
            event.creator,
            timestamp,
            offset,
            event.defaultCurrency.currencyCode,
            emptyList(),
        )
    }

    fun mapCreatedEvent(event: EventEntity): CreatedEvent = CreatedEvent(
        event.id!!,
        event.name,
        event.creator,
        Currency.getInstance(event.defaultCurrency),
        sqlTimestampMapper.mapSqlTimeStampToOffsetDateTime(event.created, event.offset),
    )

    fun mapFullEvent(event: EventEntity): FullEvent = FullEvent(
        event.id!!,
        event.name,
        event.creator,
        Currency.getInstance(event.defaultCurrency),
        sqlTimestampMapper.mapSqlTimeStampToOffsetDateTime(event.created, event.offset),
        event.payments.map { paymentEntity ->
            EventPayment(
                paymentEntity.id!!,
                paymentEntity.name,
                paymentEntity.payer,
                BigDecimal(paymentEntity.amount),
                paymentEntity.parts.map { PaymentPart(it.payee, it.part) },
                sqlTimestampMapper.mapSqlTimeStampToOffsetDateTime(paymentEntity.created, paymentEntity.offset),
            )
        },
    )
}
