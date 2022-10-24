package ru.sau.kitty_split.payment.dao

import com.vladmihalcea.hibernate.type.json.JsonType
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import ru.sau.kitty_split.event.dao.EventEntity
import java.math.BigDecimal
import java.sql.Timestamp
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType.LAZY
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType.IDENTITY
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "payments")
@TypeDef(
    name = "json",
    typeClass = JsonType::class
)
data class PaymentEntity(
    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(strategy = IDENTITY, generator = "uuid2")
    @Column(length = 36, nullable = false, updatable = false)
    val id: UUID?,
    val name: String,
    val payer: String,
    val created: Timestamp,
    @Column(name = "created_offset")
    val offset: String,
    val amount: String,
    @Type(type = "json")
    @Column(columnDefinition = "jsonb")
    val parts: List<PaymentPartEntity>,
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "event_id")
    val event: EventEntity,
    @Column(name = "event_id", insertable = false, updatable = false)
    val eventId: UUID,
)

data class PaymentPartEntity(
    val payee: String,
    val part: BigDecimal,
)
