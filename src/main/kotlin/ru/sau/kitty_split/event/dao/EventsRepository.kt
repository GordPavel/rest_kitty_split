package ru.sau.kitty_split.event.dao

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface EventsRepository : JpaRepository<EventEntity, UUID>
