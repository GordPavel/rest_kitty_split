package ru.sau.kitty_split.payment.dao

import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Propagation.MANDATORY
import org.springframework.transaction.annotation.Transactional
import ru.sau.kitty_split.event.dao.EventEntity
import ru.sau.kitty_split.payment.PaymentNotFoundException
import javax.persistence.EntityManager
import javax.persistence.criteria.CriteriaUpdate

@Repository
@Transactional(propagation = MANDATORY)
class PaymentsDao(
    private val paymentsRepository: PaymentsRepository,
    private val paymentsDaoMapper: PaymentsDaoMapper,
    private val entityManager: EntityManager,
) {

    fun save(payment: CreatePaymentDto): CreatedPaymentDto = paymentsDaoMapper
        .mapPaymentForCreation(payment, entityManager.getReference(EventEntity::class.java, payment.eventId))
        .let(paymentsRepository::save)
        .let(paymentsDaoMapper::mapCreatedPayment)

    fun updatePayment(payment: UpdatePaymentDto) {
        val criteriaUpdate: CriteriaUpdate<PaymentEntity> = paymentsDaoMapper.mapPaymentForUpdate(
            payment,
            entityManager.criteriaBuilder
        )
        val query = entityManager.createQuery(criteriaUpdate)
        val rowsUpdated = query.executeUpdate()

        when {
            rowsUpdated == 0 -> throw PaymentNotFoundException(payment.id, payment.eventId)
        }
    }

}
