package ru.sau.kitty_split.event.service

import org.springframework.stereotype.Service
import ru.sau.kitty_split.event.dao.EventsDao
import java.time.Clock
import java.time.Instant
import java.time.OffsetDateTime
import java.util.UUID

@Service
class EventsService(
    private val eventsDao: EventsDao,
    private val eventServiceMapper: EventServiceMapper,
    private val clock: Clock,
) {
    fun createEvent(
        event: CreateEvent,
    ): CreatedEvent = eventServiceMapper
        .mapCreateEventToCreateEntity(
            event,
            OffsetDateTime.ofInstant(Instant.now(clock), event.timeZone)
        )
        .let(eventsDao::save)

    fun getEvent(eventId: UUID): CreatedEvent? = eventsDao.findEventById(eventId)
}
