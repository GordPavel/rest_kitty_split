package ru.sau.kitty_split.event.dao

import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Propagation.MANDATORY
import org.springframework.transaction.annotation.Transactional
import ru.sau.kitty_split.event.service.CreatedEvent
import ru.sau.kitty_split.event.service.FullEvent
import java.util.UUID

@Repository
@Transactional(propagation = MANDATORY)
class EventsDao(
    private val eventsRepository: EventsRepository,
    private val eventsDaoMapper: EventsDaoMapper,
) {
    fun save(event: CreateEventDto): CreatedEvent = eventsDaoMapper
        .mapEventForCreation(event)
        .let(eventsRepository::save)
        .let(eventsDaoMapper::mapCreatedEvent)

    @Transactional(readOnly = true)
    fun findFullEventById(eventId: UUID): FullEvent? = eventsRepository
        .findByIdFetchPayments(eventId)
        ?.let(eventsDaoMapper::mapFullEvent)

}
