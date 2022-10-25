package ru.sau.kitty_split.payment.controller

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeInfo.As.PROPERTY
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME
import java.math.BigDecimal
import java.time.OffsetDateTime
import java.util.UUID
import javax.validation.constraints.NotNull
import javax.validation.constraints.Null

data class CreatePaymentControllerDto(
    @Null
    val id: UUID?,
    @NotNull
    val name: String,
    @NotNull
    val payer: String,
    @NotNull
    val amount: PaymentAmountDto,
    val currency: String?,
)

data class CreatedPaymentControllerDto(
    val id: UUID,
    val eventId: UUID,
    val name: String,
    val payer: String,
    val amount: PaymentAmountsAmountDto,
    val currency: String,
    val created: OffsetDateTime,
)

data class UpdatePaymentControllerDto(
    val name: String?,
    val payer: String?,
    val amount: PaymentAmountDto?,
    val currency: String?,
    val created: OffsetDateTime?,
)

@JsonTypeInfo(use = NAME, property = "type", include = PROPERTY)
@JsonSubTypes(
    JsonSubTypes.Type(value = PaymentEqualAmountDto::class, name = "equal"),
    JsonSubTypes.Type(value = PaymentPartsAmountDto::class, name = "parts"),
    JsonSubTypes.Type(value = PaymentAmountsAmountDto::class, name = "amounts"),
)
sealed interface PaymentAmountDto

data class PaymentEqualAmountDto(
    val totalAmount: BigDecimal,
) : PaymentAmountDto

data class PaymentPartsAmountDto(
    val totalAmount: BigDecimal,
    val spentParts: Map<String, BigDecimal>,
) : PaymentAmountDto

data class PaymentAmountsAmountDto(
    val spentAmounts: Map<String, BigDecimal>,
) : PaymentAmountDto
