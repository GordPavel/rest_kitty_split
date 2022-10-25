package ru.sau.kitty_split.payment.service

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingConstants.ComponentModel
import ru.sau.kitty_split.payment.dao.CreatePaymentDto
import ru.sau.kitty_split.payment.dao.CreatedPaymentDto
import ru.sau.kitty_split.payment.dao.UpdatePaymentDto
import java.math.BigDecimal
import java.time.OffsetDateTime
import java.util.Currency

@Mapper(componentModel = ComponentModel.SPRING)
abstract class PaymentsServiceMapper {

    @Mapping(source = "payment.name", target = "name")
    @Mapping(source = "payment.payer", target = "payer")
    @Mapping(source = "payment.eventId", target = "eventId")
    @Mapping(source = "created", target = "created")
    @Mapping(source = "spentAmounts", target = "spentAmounts")
    abstract fun mapCreatePaymentToCreateEntity(
        payment: CreatePayment,
        spentAmounts: Map<String, BigDecimal>,
        created: OffsetDateTime,
    ): CreatePaymentDto

    fun mapCreatedPaymentFromCreateEntity(payment: CreatedPaymentDto, currency: Currency): CreatedPayment =
        CreatedPayment(
            payment.id,
            payment.eventId,
            payment.name,
            payment.payer,
            PaymentAmountsAmount(payment.spentAmounts),
            currency = currency,
            payment.created,
        )

    @Mapping(source = "payment.id", target = "id")
    @Mapping(source = "payment.name", target = "name")
    @Mapping(source = "payment.payer", target = "payer")
    @Mapping(source = "payment.eventId", target = "eventId")
    @Mapping(source = "payment.created", target = "created")
    @Mapping(source = "spentAmounts", target = "spentAmounts")
    abstract fun mapUpdatePaymentToUpdateEntity(
        payment: UpdatePayment,
        spentAmounts: Map<String, BigDecimal>?,
    ): UpdatePaymentDto

}
