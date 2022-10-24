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
    @Mapping(source = "amount", target = "amount")
    abstract fun mapCreatePaymentToCreateEntity(
        payment: CreatePayment,
        created: OffsetDateTime,
        amount: BigDecimal,
    ): CreatePaymentDto

    fun mapCreatedPaymentFromCreateEntity(payment: CreatedPaymentDto, currency: Currency): CreatedPayment =
        CreatedPayment(
            payment.id,
            payment.name,
            payment.payer,
            CreatedPaymentAmount(
                payment.amount,
                currency,
            ),
            parts = payment.parts,
            payment.created,
            payment.eventId,
        )

    @Mapping(source = "amount", target = "amount")
    abstract fun mapUpdatePaymentToUpdateEntity(
        payment: UpdatePayment,
        amount: BigDecimal?,
    ): UpdatePaymentDto

}
