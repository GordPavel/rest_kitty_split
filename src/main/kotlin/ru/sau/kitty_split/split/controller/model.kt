package ru.sau.kitty_split.split.controller

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.math.BigDecimal
import java.util.Currency

@ApiModel(description = "Транзакция")
data class TransactionDto(
    @ApiModelProperty(
        value = "Отправитель денег",
        required = true,
    )
    val payer: String,
    @ApiModelProperty(
        value = "Получатель денег",
        required = true,
    )
    val payee: String,
    @ApiModelProperty(
        value = "Сумма денег",
        required = true,
    )
    val amount: TransactionAmount,
)

@ApiModel(description = "Сумма транзакции")
data class TransactionAmount(
    @ApiModelProperty(
        value = "Сумма денег",
        required = true,
    )
    val amount: BigDecimal,
    @ApiModelProperty(
        value = "Валюта",
        required = true,
    )
    val currency: Currency,
)
