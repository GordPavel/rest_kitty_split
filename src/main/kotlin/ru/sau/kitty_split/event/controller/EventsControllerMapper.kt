package ru.sau.kitty_split.event.controller

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingConstants.ComponentModel.SPRING
import ru.sau.kitty_split.event.service.CreateEvent
import ru.sau.kitty_split.event.service.CreatedEvent
import java.time.ZoneId

@Mapper(componentModel = SPRING)
interface EventsControllerMapper {
    @Mapping(source = "zoneId", target = "timeZone")
    fun fromControllerDto(event: CreateEventControllerDto, zoneId: ZoneId): CreateEvent
    fun toControllerDto(event: CreatedEvent): CreatedEventControllerDto
}
