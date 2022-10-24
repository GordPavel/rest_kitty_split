package ru.sau.kitty_split.payment.dao

import org.springframework.stereotype.Service
import ru.sau.kitty_split.event.dao.EventEntity
import ru.sau.kitty_split.payment.service.PaymentPart
import ru.sau.kitty_split.util.SqlTimestampMapper
import java.math.BigDecimal
import java.sql.Timestamp
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaUpdate
import javax.persistence.criteria.Expression
import javax.persistence.criteria.Root

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

    fun mapPaymentForUpdate(
        payment: UpdatePaymentDto,
        criteriaBuilder: CriteriaBuilder
    ): CriteriaUpdate<PaymentEntity> {
        val update: CriteriaUpdate<PaymentEntity> = criteriaBuilder.createCriteriaUpdate(PaymentEntity::class.java)
        val root: Root<PaymentEntity> = update.from(PaymentEntity::class.java)
        update.where(
            criteriaBuilder.and(
                criteriaBuilder.equal(root.get<Expression<*>>("id"), payment.id),
                criteriaBuilder.equal(root.get<Expression<*>>("eventId"), payment.eventId)
            )
        )

        payment.name?.run { update.set("name", this) }
        payment.payer?.run { update.set("payer", this) }
        payment.amount?.run { update.set("amount", this.toString()) }
        payment.created
            ?.let(sqlTimestampMapper::mapOffsetDateTimeToSqlTimeStamp)
            ?.run {
                val (timestamp: Timestamp, offset: String) = this
                update.set("created", timestamp)
                update.set("offset", offset)
            }
        payment.parts
            ?.let { parts -> parts.map { PaymentPart(it.payee, it.part) } }
            ?.run { update.set("parts", this) }

        return update
    }
}
