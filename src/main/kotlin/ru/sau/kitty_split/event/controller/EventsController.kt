package ru.sau.kitty_split.event.controller

import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
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
import ru.sau.kitty_split.commons.ApiErrorResponse
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

    @ApiOperation(value = "Создание нового события")
    @ApiResponses(
        ApiResponse(code = 201, response = CreatedEventControllerDto::class, message = "Событие успешно создано"),
        ApiResponse(code = 400, response = ApiErrorResponse::class, message = "Ошибка входных данных"),
        ApiResponse(code = 500, response = ApiErrorResponse::class, message = "Внутренняя ошибка сервера"),
    )
    @PostMapping
    @ResponseStatus(CREATED)
    fun createEvent(
        @Valid @RequestBody eventDto: CreateEventControllerDto,
        @ApiParam(
            name = "Зона часового пояса",
            type = "java.time.ZoneId",
            defaultValue = "Europe/Moscow",
            required = false,
        )
        @RequestHeader(value = "timezone", required = false) headerTimezone: String?,
    ): CreatedEventControllerDto = eventsControllerMapper
        .fromControllerDto(
            eventDto,
            parseTimeZone(headerTimezone, defaultTimeZone),
        )
        .let(eventsService::createEvent)
        .let(eventsControllerMapper::toControllerDto)

    @ApiOperation(value = "Получение события")
    @ApiResponses(
        ApiResponse(code = 200, response = EventFullControllerDto::class, message = "Событие успешно найдено"),
        ApiResponse(code = 400, response = ApiErrorResponse::class, message = "Ошибка входных данных"),
        ApiResponse(code = 404, response = ApiErrorResponse::class, message = "Сущность не найдена"),
        ApiResponse(code = 500, response = ApiErrorResponse::class, message = "Внутренняя ошибка сервера"),
    )
    @GetMapping("/{eventId}")
    fun getEvent(
        @ApiParam(
            name = "Id события",
            example = "576ac17b-c5b9-4bb7-a33f-317e38d03044",
            required = true,
            type = "java.util.UUID",
        )
        @PathVariable eventId: UUID,
    ): EventFullControllerDto = eventsService
        .getEvent(eventId)
        ?.let(eventsControllerMapper::toControllerDto)
        ?: throw EventNotFoundException(eventId)

}
