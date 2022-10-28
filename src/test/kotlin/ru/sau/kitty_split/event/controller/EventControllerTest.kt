package ru.sau.kitty_split.event.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.mapstruct.factory.Mappers
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import ru.sau.kitty_split.configuration.MvcConfiguration
import ru.sau.kitty_split.event.service.CreatedEvent
import ru.sau.kitty_split.event.service.EventsService
import ru.sau.kitty_split.util.FixedClockTestConfiguration
import java.nio.charset.StandardCharsets.UTF_8
import java.time.OffsetDateTime
import java.time.ZoneId
import java.util.UUID
import java.time.Instant.parse as parseInstant
import java.util.Currency.getInstance as parseCurrency

@AutoConfigureMockMvc
@WebMvcTest(EventsController::class)
@Import(
    EventControllerTestConfiguration::class,
    MvcConfiguration::class,
    FixedClockTestConfiguration::class,
)
@ActiveProfiles("test")
class EventControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockkBean
    lateinit var eventService: EventsService

    @Autowired
    lateinit var eventsControllerMapper: EventsControllerMapper

    @Value("\${defaults.time.zone}")
    lateinit var defaultZoneId: ZoneId

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Test
    internal fun `given event service creates correct event when make post request then response is correct get correct arguments`() {
//        given
        val createEventDto = CreateEventControllerDto(
            null,
            "event",
            "author",
            "RUB",
            listOf("Паша"),
        )
        val createdEvent = CreatedEvent(
            UUID.randomUUID(),
            "name",
            "creator",
            parseCurrency("RUB"),
            OffsetDateTime.ofInstant(parseInstant("2022-01-01T00:00:00.000000Z"), defaultZoneId),
            participants = listOf("Паша"),
        )
        val expectedCreateEvent = createEventDto.let {
            eventsControllerMapper.fromControllerDto(
                it,
                defaultZoneId,
            )
        }
        every { eventService.createEvent(eq(expectedCreateEvent)) } returns createdEvent

//        when
        mockMvc
            .perform(
                post("/events")
                    .contentType(APPLICATION_JSON_VALUE)
                    .accept(APPLICATION_JSON)
                    .characterEncoding(UTF_8)
                    .content(objectMapper.writeValueAsString(createEventDto)),
            )

//        then
            .andExpect(status().`is`(CREATED.value()))
            .andExpect(
                content().json(
                    createdEvent
                        .let(eventsControllerMapper::toControllerDto)
                        .let(objectMapper::writeValueAsString)
                )
            )

        verify(exactly = 1) { eventService.createEvent(eq(expectedCreateEvent)) }
    }
}

@TestConfiguration
private class EventControllerTestConfiguration {
    @Bean
    fun mapper(): EventsControllerMapper = Mappers.getMapper(EventsControllerMapper::class.java)
}
