package ru.sau.kitty_split.event.controller

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.math.BigDecimal
import java.time.OffsetDateTime
import java.util.UUID
import javax.validation.constraints.NotNull
import javax.validation.constraints.Null

@ApiModel(description = "Сущность создания нового события")
data class CreateEventControllerDto(
    @ApiModelProperty(
        hidden = true,
    )
    @Null
    val id: UUID?,
    @ApiModelProperty(
        value = "Название события",
        required = true,
    )
    @NotNull
    val name: String,
    @ApiModelProperty(
        value = "Ник создателя",
        required = true,
    )
    @NotNull
    val creator: String,
    @ApiModelProperty(
        value = "Код валюты по-умолчанию, к которой будут приводиться все платежи в других валютах. Стандарт ISO 4217",
        required = true,
    )
    @NotNull
    val defaultCurrency: String,
    @ApiModelProperty(
        value = "Никнеймы участников события",
        required = true,
    )
    @NotNull
    val participants: List<String>,
)

@ApiModel(description = "Сущность созданного события")
data class CreatedEventControllerDto(
    @ApiModelProperty(
        value = "id события",
        required = true,
    )
    val id: UUID,
    @ApiModelProperty(
        value = "Название события",
        required = true,
    )
    val name: String,
    @ApiModelProperty(
        value = "Ник создателя",
        required = true,
    )
    val creator: String,
    @ApiModelProperty(
        value = "Код валюты по-умолчанию, к которой будут приводиться все платежи в других валютах. Стандарт ISO 4217",
        required = true,
    )
    val defaultCurrency: String,
    @ApiModelProperty(
        value = "Никнеймы участников события",
        required = true,
    )
    val participants: List<String>,
    @ApiModelProperty(
        value = "Время создания события",
        required = true,
    )
    val created: OffsetDateTime,
)

@ApiModel(description = "Полная сущность события")
data class EventFullControllerDto(
    @ApiModelProperty(
        value = "id события",
        required = true,
    )
    val id: UUID,
    @ApiModelProperty(
        value = "Название события",
        required = true,
    )
    val name: String,
    @ApiModelProperty(
        value = "Ник создателя",
        required = true,
    )
    val creator: String,
    @ApiModelProperty(
        value = "Код валюты по-умолчанию, к которой будут приводиться все платежи в других валютах. Стандарт ISO 4217",
        required = true,
    )
    val defaultCurrency: String,
    @ApiModelProperty(
        value = "Время создания события",
        required = true,
    )
    val created: OffsetDateTime,
    @ApiModelProperty(
        value = "Никнеймы участников события",
        required = true,
    )
    val participants: List<String>,
    @ApiModelProperty(
        value = "Список платежей",
        required = true,
    )
    val payments: List<EventFullDtoPayment>,
)

@ApiModel(description = "Сущность платежа внутри события")
data class EventFullDtoPayment(
    @ApiModelProperty(
        value = "id платежа",
        required = true,
    )
    val id: UUID,
    @ApiModelProperty(
        value = "Что было оплачено",
        required = true,
    )
    val name: String,
    @ApiModelProperty(
        value = "Никнейм плательщика",
        required = true,
    )
    val payer: String,
    @ApiModelProperty(
        value = "На кого из участников сколько было потрачено",
        required = true,
    )
    val spentAmounts: Map<String, BigDecimal>,
    @ApiModelProperty(
        value = "Время создания платежа",
        required = true,
    )
    val created: OffsetDateTime,
)
