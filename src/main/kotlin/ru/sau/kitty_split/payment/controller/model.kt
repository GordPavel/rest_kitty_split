package ru.sau.kitty_split.payment.controller

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeInfo.As.PROPERTY
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.math.BigDecimal
import java.time.OffsetDateTime
import java.util.UUID
import javax.validation.constraints.NotNull
import javax.validation.constraints.Null

@ApiModel(description = "Сущность создания нового платежа")
data class CreatePaymentControllerDto(
    @ApiModelProperty(
        hidden = true,
    )
    @Null
    val id: UUID?,
    @ApiModelProperty(
        value = "Что было оплачено",
        required = true,
    )
    @NotNull
    val name: String,
    @ApiModelProperty(
        value = "Никнейм плательщика",
        required = true,
    )
    @NotNull
    val payer: String,
    @ApiModelProperty(
        value = "Способ разделения между участниками вместе с суммой",
        required = true,
        dataType = "ru.sau.kitty_split.payment.controller.PaymentAmountsAmountDto",
        example = """
            {
                "type": "amounts",
                "spentAmounts": {
                    "payeeNickname1" : "500.0",
                    "payeeNickname2" : "300.0"
                }
            }
        """,
    )
    @NotNull
    val amount: PaymentAmountDto,
    @ApiModelProperty(
        value = "Альтернативная валюта, если отличается от валюты по-умолчанию события",
        required = false,
    )
    val currency: String?,
)

@ApiModel(description = "Сущность созданного платежа")
data class CreatedPaymentControllerDto(
    @ApiModelProperty(
        value = "Id платежа",
        required = true,
    )
    val id: UUID,
    @ApiModelProperty(
        value = "Id события",
        required = true,
    )
    val eventId: UUID,
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
        value = "Сколько было потрачено на кого из участников",
        required = true,
    )
    val amount: PaymentAmountsAmountDto,
    @ApiModelProperty(
        value = "Валюта платежа",
        required = true,
    )
    val currency: String,
    @ApiModelProperty(
        value = "Время создания платежа",
        required = true,
    )
    val created: OffsetDateTime,
)

@ApiModel(description = "Сущность обновления платежа")
data class UpdatePaymentControllerDto(
    @ApiModelProperty(
        value = "Что было оплачено",
        required = false,
    )
    val name: String?,
    @ApiModelProperty(
        value = "Никнейм плательщика",
        required = false,
    )
    val payer: String?,
    @ApiModelProperty(
        value = "Сколько было потрачено на кого из участников",
        required = false,
        example = """
            {
                "type": "amounts",
                "spentAmounts": {
                    "payeeNickname1" : "500.0",
                    "payeeNickname2" : "300.0"
                }
            }
        """,
    )
    val amount: PaymentAmountDto?,
    @ApiModelProperty(
        value = "Валюта платежа",
        required = false,
    )
    val currency: String?,
    @ApiModelProperty(
        value = "Время создания платежа",
        required = false,
    )
    val created: OffsetDateTime?,
)

@JsonTypeInfo(use = NAME, property = "type", include = PROPERTY)
@JsonSubTypes(
    JsonSubTypes.Type(value = PaymentEqualAmountDto::class, name = "equal"),
    JsonSubTypes.Type(value = PaymentPartsAmountDto::class, name = "parts"),
    JsonSubTypes.Type(value = PaymentAmountsAmountDto::class, name = "amounts"),
)
sealed interface PaymentAmountDto

@ApiModel(description = "Тип платежа за всех участников по-ровну")
data class PaymentEqualAmountDto(
    @ApiModelProperty(
        value = "Сколько было потрачено денег всего",
        required = true,
    )
    val totalAmount: BigDecimal,
) : PaymentAmountDto

@ApiModel(description = "Тип платежа за указанных участников в пропорциях")
data class PaymentPartsAmountDto(
    @ApiModelProperty(
        value = "Сколько было потрачено денег всего",
        required = true,
    )
    val totalAmount: BigDecimal,
    @ApiModelProperty(
        value = "Участники с указанием доли каждого в платеже",
        required = true,
    )
    val spentParts: Map<String, BigDecimal>,
) : PaymentAmountDto

@ApiModel(description = "Тип платежа за указанных участников с указанными суммами")
data class PaymentAmountsAmountDto(
    @ApiModelProperty(
        value = "Участники с указанием суммы каждого в платеже",
        required = true,
    )
    val spentAmounts: Map<String, BigDecimal>,
) : PaymentAmountDto
