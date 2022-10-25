package ru.sau.kitty_split.event.dao

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingConstants.ComponentModel.SPRING
import ru.sau.kitty_split.event.service.CreatedEvent
import ru.sau.kitty_split.event.service.FullEvent

@Mapper(componentModel = SPRING)
interface EventsDaoMapper {

    @Mapping(target = "id", expression = "java(null)")
    @Mapping(target = "payments", expression = "java(List.of())")
    fun mapEventForCreation(event: CreateEventDto): EventEntity

    fun mapCreatedEvent(event: EventEntity): CreatedEvent

    fun mapFullEvent(event: EventEntity): FullEvent
}
