package ru.sau.kitty_split.payment.controller

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingConstants.ComponentModel
import ru.sau.kitty_split.IncorrectCurrencyCodeException
import ru.sau.kitty_split.payment.service.CreatePayment
import ru.sau.kitty_split.payment.service.CreatedPayment
import ru.sau.kitty_split.payment.service.UpdatePayment
import java.time.ZoneId
import java.util.UUID

@Mapper(componentModel = ComponentModel.SPRING)
abstract class PaymentsControllerMapper {

    fun fromControllerDto(payment: CreatePaymentControllerDto, eventId: UUID, zoneId: ZoneId): CreatePayment = try {
        fromControllerDtoInternal(payment, eventId, zoneId)
    } catch (ex: IllegalArgumentException) {
        throw IncorrectCurrencyCodeException(payment.amount.currency!!, ex)
    }

    @Mapping(source = "zoneId", target = "timeZone")
    protected abstract fun fromControllerDtoInternal(
        payment: CreatePaymentControllerDto,
        eventId: UUID,
        zoneId: ZoneId
    ): CreatePayment

    abstract fun toControllerDto(payment: CreatedPayment): CreatedPaymentControllerDto

    fun fromControllerDto(
        payment: UpdatePaymentControllerDto,
        id: UUID,
        eventId: UUID,
    ): UpdatePayment = try {
        fromControllerDtoInternal(payment, id, eventId)
    } catch (ex: IllegalArgumentException) {
        throw IncorrectCurrencyCodeException(payment.amount!!.currency!!, ex)
    }

    protected abstract fun fromControllerDtoInternal(
        payment: UpdatePaymentControllerDto,
        id: UUID,
        eventId: UUID,
    ): UpdatePayment
}
