package ru.sau.kitty_split.util

import org.mapstruct.Mapper
import org.mapstruct.MappingConstants.ComponentModel.SPRING
import java.sql.Timestamp
import java.time.OffsetDateTime
import java.time.ZoneOffset

@Mapper(componentModel = SPRING)
abstract class SqlTimestampMapper {
    fun mapOffsetDateTimeToSqlTimeStamp(timestamp: OffsetDateTime): Pair<Timestamp, String> =
        Timestamp.from(timestamp.toInstant()) to timestamp.offset.id

    fun mapSqlTimeStampToOffsetDateTime(timestamp: Timestamp, offset: String): OffsetDateTime =
        OffsetDateTime.of(timestamp.toLocalDateTime(), ZoneOffset.of(offset))
}
