package ru.sau.kitty_split.event.controller

import ru.sau.kitty_split.payment.controller.PaymentPartDto
import java.math.BigDecimal
import java.time.OffsetDateTime
import java.util.UUID
import javax.validation.constraints.NotNull
import javax.validation.constraints.Null

data class CreateEventControllerDto(
    @Null
    val id: UUID?,
    @NotNull
    val name: String,
    @NotNull
    val creator: String,
    @NotNull
    val defaultCurrency: String,
)

data class CreatedEventControllerDto(
    val id: UUID,
    val name: String,
    val creator: String,
    val defaultCurrency: String,
    val created: OffsetDateTime,
)

data class EventFullControllerDto(
    val id: UUID,
    val name: String,
    val creator: String,
    val defaultCurrency: String,
    val created: OffsetDateTime,
    val payments: List<EventFullDtoPayment>,
)

data class EventFullDtoPayment(
    val id: UUID,
    val name: String,
    val payer: String,
    val amount: BigDecimal,
    val parts: List<PaymentPartDto>,
    val created: OffsetDateTime,
)
