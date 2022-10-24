package ru.sau.kitty_split.payment.dao

import org.springframework.stereotype.Repository
import ru.sau.kitty_split.event.dao.EventEntity
import javax.persistence.EntityManager

@Repository
class PaymentsDao(
    private val paymentsRepository: PaymentsRepository,
    private val paymentsDaoMapper: PaymentsDaoMapper,
    private val entityManager: EntityManager,
) {

    fun save(payment: CreatePaymentDto): CreatedPaymentDto = paymentsDaoMapper
        .mapPaymentForCreation(payment, entityManager.getReference(EventEntity::class.java, payment.eventId))
        .let(paymentsRepository::save)
        .let(paymentsDaoMapper::mapCreatedPayment)

}
