package ru.sau.kitty_split.util

import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.time.OffsetDateTime
import java.time.ZoneOffset

@Service
class SqlTimestampMapper {
    fun mapOffsetDateTimeToSqlTimeStamp(timestamp: OffsetDateTime): Pair<Timestamp, String> =
        Timestamp.from(timestamp.toInstant()) to timestamp.offset.id

    fun mapSqlTimeStampToOffsetDateTime(timestamp: Timestamp, offset: String): OffsetDateTime =
        OffsetDateTime.of(timestamp.toLocalDateTime(), ZoneOffset.of(offset))
}
