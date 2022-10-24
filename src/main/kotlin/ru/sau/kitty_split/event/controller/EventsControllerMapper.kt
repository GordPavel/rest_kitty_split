package ru.sau.kitty_split.event.controller

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingConstants.ComponentModel.SPRING
import ru.sau.kitty_split.IncorrectCurrencyCodeException
import ru.sau.kitty_split.event.service.CreateEvent
import ru.sau.kitty_split.event.service.CreatedEvent
import ru.sau.kitty_split.event.service.FullEvent
import java.time.ZoneId

@Mapper(componentModel = SPRING)
abstract class EventsControllerMapper {

    fun fromControllerDto(event: CreateEventControllerDto, zoneId: ZoneId): CreateEvent = try {
        fromControllerDtoInternal(event, zoneId)
    } catch (ex: IllegalArgumentException) {
        throw IncorrectCurrencyCodeException(event.defaultCurrency, ex)
    }

    @Mapping(source = "zoneId", target = "timeZone")
    abstract fun fromControllerDtoInternal(event: CreateEventControllerDto, zoneId: ZoneId): CreateEvent
    abstract fun toControllerDto(event: CreatedEvent): CreatedEventControllerDto

    abstract fun toControllerDto(event: FullEvent): EventFullControllerDto
}
