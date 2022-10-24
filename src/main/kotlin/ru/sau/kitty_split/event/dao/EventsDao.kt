package ru.sau.kitty_split.event.dao

import org.springframework.stereotype.Repository
import ru.sau.kitty_split.event.service.CreatedEvent
import ru.sau.kitty_split.event.service.FullEvent
import java.util.UUID

@Repository
class EventsDao(
    private val eventsRepository: EventsRepository,
    private val eventsDaoMapper: EventsDaoMapper,
) {
    fun save(event: CreateEventDto): CreatedEvent = eventsDaoMapper
        .mapEventForCreation(event)
        .let(eventsRepository::save)
        .let(eventsDaoMapper::mapCreatedEvent)

    fun findFullEventById(eventId: UUID): FullEvent? = eventsRepository
        .findByIdFetchPayments(eventId)
        ?.let(eventsDaoMapper::mapFullEvent)

}
