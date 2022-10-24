package ru.sau.kitty_split.event.dao

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.UUID

interface EventsRepository : JpaRepository<EventEntity, UUID> {
    @Query("select e from EventEntity e left join fetch e.payments p where e.id = :id")
    fun findByIdFetchPayments(id: UUID): EventEntity?
}
