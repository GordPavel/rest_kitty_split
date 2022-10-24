package ru.sau.kitty_split.payment.dao

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface PaymentsRepository : JpaRepository<PaymentEntity, UUID> {
}
