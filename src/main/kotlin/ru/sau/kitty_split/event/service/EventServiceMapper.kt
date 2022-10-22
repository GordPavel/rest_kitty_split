package ru.sau.kitty_split.event.service

import org.mapstruct.Mapper
import org.mapstruct.MappingConstants.ComponentModel.SPRING
import ru.sau.kitty_split.event.dao.CreateEventDto
import java.time.OffsetDateTime

@Mapper(componentModel = SPRING)
interface EventServiceMapper {
    fun mapCreateEventToCreateEntity(event: CreateEvent, created: OffsetDateTime): CreateEventDto
}
