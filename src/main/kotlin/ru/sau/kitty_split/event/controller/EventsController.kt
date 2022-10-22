package ru.sau.kitty_split.event.controller

import org.springframework.beans.factory.annotation.Value
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.sau.kitty_split.event.service.EventsService
import java.time.DateTimeException
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
    fun createEvent(
        @Valid @RequestBody eventDto: CreateEventControllerDto,
        @RequestHeader(value = "timezone", required = false) headerTimezone: String?,
    ): CreatedEventControllerDto = eventsControllerMapper
        .fromControllerDto(
            eventDto,
            parseTimeZone(headerTimezone),
        )
        .let(eventsService::createEvent)
        .let(eventsControllerMapper::toControllerDto)

    private fun parseTimeZone(headerTimezone: String?) = try {
        headerTimezone?.let(ZoneId::of) ?: defaultTimeZone
    } catch (e: DateTimeException) {
        throw InvalidTimeZoneException(headerTimezone!!, e)
    }

    @GetMapping("/{eventId}")
    fun getEvent(
        @PathVariable eventId: UUID,
    ): CreatedEventControllerDto = eventsService
        .getEvent(eventId)
        ?.let(eventsControllerMapper::toControllerDto)
        ?: throw EventNotFoundException(eventId)

}
