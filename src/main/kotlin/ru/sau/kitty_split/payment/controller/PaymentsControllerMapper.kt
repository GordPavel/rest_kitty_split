package ru.sau.kitty_split.payment.controller

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingConstants.ComponentModel
import ru.sau.kitty_split.IncorrectCurrencyCodeException
import ru.sau.kitty_split.payment.service.CreatePayment
import ru.sau.kitty_split.payment.service.CreatedPayment
import ru.sau.kitty_split.payment.service.PaymentAmount
import ru.sau.kitty_split.payment.service.PaymentAmountsAmount
import ru.sau.kitty_split.payment.service.PaymentEqualAmount
import ru.sau.kitty_split.payment.service.PaymentPartsAmount
import ru.sau.kitty_split.payment.service.UpdatePayment
import java.time.ZoneId
import java.util.UUID

@Mapper(componentModel = ComponentModel.SPRING)
abstract class PaymentsControllerMapper {

    fun fromControllerDto(payment: CreatePaymentControllerDto, eventId: UUID, zoneId: ZoneId): CreatePayment = try {
        fromControllerDtoInternal(payment, eventId, zoneId)
    } catch (ex: IllegalArgumentException) {
        throw IncorrectCurrencyCodeException(payment.currency!!, ex)
    }

    @Mapping(source = "zoneId", target = "timeZone")
    protected abstract fun fromControllerDtoInternal(
        payment: CreatePaymentControllerDto,
        eventId: UUID,
        zoneId: ZoneId
    ): CreatePayment

    protected fun mapAmount(amount: PaymentAmountDto): PaymentAmount = when (amount) {
        is PaymentEqualAmountDto   -> mapAmount(amount)
        is PaymentPartsAmountDto   -> mapAmount(amount)
        is PaymentAmountsAmountDto -> mapAmount(amount)
    }

    protected abstract fun mapAmount(amount: PaymentEqualAmountDto): PaymentEqualAmount
    protected abstract fun mapAmount(amount: PaymentPartsAmountDto): PaymentPartsAmount
    protected abstract fun mapAmount(amount: PaymentAmountsAmountDto): PaymentAmountsAmount

    abstract fun toControllerDto(payment: CreatedPayment): CreatedPaymentControllerDto

    fun fromControllerDto(
        payment: UpdatePaymentControllerDto,
        id: UUID,
        eventId: UUID,
    ): UpdatePayment = try {
        fromControllerDtoInternal(payment, id, eventId)
    } catch (ex: IllegalArgumentException) {
        throw IncorrectCurrencyCodeException(payment.currency!!, ex)
    }

    protected abstract fun fromControllerDtoInternal(
        payment: UpdatePaymentControllerDto,
        id: UUID,
        eventId: UUID,
    ): UpdatePayment
}
