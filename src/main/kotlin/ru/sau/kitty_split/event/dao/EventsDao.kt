package ru.sau.kitty_split.event.dao

import org.springframework.stereotype.Repository
import ru.sau.kitty_split.event.service.CreatedEvent
import java.util.Optional
import java.util.UUID

fun <T> Optional<T>.unwrap(): T? = orElse(null)

@Repository
class EventsDao(
    private val eventsRepository: EventsRepository,
    private val eventsDaoMapper: EventsDaoMapper,
) {
    fun save(event: CreateEventDto): CreatedEvent = eventsDaoMapper
        .mapEventForCreation(event)
        .let(eventsRepository::save)
        .let(eventsDaoMapper::mapCreatedEvent)

    fun findEventById(eventId: UUID): CreatedEvent? = eventsRepository
        .findById(eventId)
        .map(eventsDaoMapper::mapCreatedEvent)
        .unwrap()

}
