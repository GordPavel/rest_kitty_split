package ru.sau.kitty_split.payment.dao

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import java.util.UUID

interface PaymentsRepository : JpaRepository<PaymentEntity, UUID> {

    @Modifying
    fun deleteByIdAndEventId(id: UUID, eventId: UUID): Long

}
