package ru.sau.kitty_split.event.dao

import org.springframework.stereotype.Service
import ru.sau.kitty_split.event.service.CreatedEvent
import ru.sau.kitty_split.util.SqlTimestampMapper
import java.util.Currency

@Service
class EventsDaoMapper(
    private val sqlTimestampMapper: SqlTimestampMapper,
) {

    fun mapEventForCreation(event: CreateEventDto): EventEntity {
        val (timestamp, offset) = sqlTimestampMapper.mapOffsetDateTimeToSqlTimeStamp(event.created)
        return EventEntity(
            null,
            event.name,
            event.creator,
            timestamp,
            offset,
            event.defaultCurrency.currencyCode
        )
    }

    fun mapCreatedEvent(event: EventEntity): CreatedEvent {
        val created = sqlTimestampMapper.mapSqlTimeStampToOffsetDateTime(event.created, event.offset)
        return CreatedEvent(
            event.id!!,
            event.name,
            event.creator,
            Currency.getInstance(event.defaultCurrency),
            created
        )
    }
}
