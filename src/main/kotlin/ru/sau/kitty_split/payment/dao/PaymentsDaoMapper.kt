package ru.sau.kitty_split.payment.dao

import org.springframework.stereotype.Service
import ru.sau.kitty_split.event.dao.EventEntity
import ru.sau.kitty_split.payment.service.PaymentPart
import ru.sau.kitty_split.util.SqlTimestampMapper
import java.math.BigDecimal
import java.sql.Timestamp

@Service
class PaymentsDaoMapper(
    private val sqlTimestampMapper: SqlTimestampMapper,
) {
    fun mapPaymentForCreation(payment: CreatePaymentDto, event: EventEntity): PaymentEntity {
        val (timestamp: Timestamp, offset: String) = sqlTimestampMapper.mapOffsetDateTimeToSqlTimeStamp(payment.created)
        return PaymentEntity(
            null,
            payment.name,
            payment.payer,
            timestamp,
            offset,
            payment.amount.toString(),
            parts = payment.parts.map { PaymentPartEntity(it.payee, it.part) },
            event,
            event.id!!,
        )
    }

    fun mapCreatedPayment(payment: PaymentEntity): CreatedPaymentDto = CreatedPaymentDto(
        payment.id!!,
        payment.name,
        payment.payer,
        BigDecimal(payment.amount),
        payment.parts.map { PaymentPart(it.payee, it.part) },
        sqlTimestampMapper.mapSqlTimeStampToOffsetDateTime(payment.created, payment.offset),
        payment.eventId,
    )
}
