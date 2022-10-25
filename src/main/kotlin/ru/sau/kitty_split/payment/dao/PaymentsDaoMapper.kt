package ru.sau.kitty_split.payment.dao

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingConstants.ComponentModel
import ru.sau.kitty_split.event.dao.EventEntity
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaUpdate
import javax.persistence.criteria.Expression
import javax.persistence.criteria.Root

@Mapper(componentModel = ComponentModel.SPRING)
abstract class PaymentsDaoMapper {
    @Mapping(source = "payment.name", target = "name")
    @Mapping(source = "payment.payer", target = "payer")
    @Mapping(source = "payment.created", target = "created")
    @Mapping(source = "payment.spentAmounts", target = "spentAmounts")
    @Mapping(source = "event", target = "event")
    @Mapping(source = "event.id", target = "eventId")
    abstract fun mapPaymentForCreation(payment: CreatePaymentDto, event: EventEntity): PaymentEntity

    abstract fun mapCreatedPayment(payment: PaymentEntity): CreatedPaymentDto

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
        payment.spentAmounts?.run { update.set("spentAmounts", this.toString()) }
        payment.created?.run { update.set("created", this) }
        return update
    }
}
