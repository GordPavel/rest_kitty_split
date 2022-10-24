package ru.sau.kitty_split.event.controller

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus.CREATED
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import ru.sau.kitty_split.commons.parseTimeZone
import ru.sau.kitty_split.event.EventNotFoundException
import ru.sau.kitty_split.event.service.EventsService
import java.time.ZoneId
import java.util.UUID
import javax.validation.Valid

@RestController
@RequestMapping("/events")
@Validated
internal class EventsController(
    @Value("\${defaults.time.zone}")
    private val defaultTimeZone: ZoneId,
    private val eventsControllerMapper: EventsControllerMapper,
    private val eventsService: EventsService,
) {

    @PostMapping
    @ResponseStatus(CREATED)
    fun createEvent(
        @Valid @RequestBody eventDto: CreateEventControllerDto,
        @RequestHeader(value = "timezone", required = false) headerTimezone: String?,
    ): CreatedEventControllerDto = eventsControllerMapper
        .fromControllerDto(
            eventDto,
            parseTimeZone(headerTimezone, defaultTimeZone),
        )
        .let(eventsService::createEvent)
        .let(eventsControllerMapper::toControllerDto)

    @GetMapping("/{eventId}")
    fun getEvent(
        @PathVariable eventId: UUID,
    ): EventFullControllerDto = eventsService
        .getEvent(eventId)
        ?.let(eventsControllerMapper::toControllerDto)
        ?: throw EventNotFoundException(eventId)

}
