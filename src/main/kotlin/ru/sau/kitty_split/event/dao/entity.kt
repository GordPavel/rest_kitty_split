package ru.sau.kitty_split.event.dao

import org.hibernate.annotations.GenericGenerator
import ru.sau.kitty_split.payment.dao.PaymentEntity
import java.sql.Timestamp
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType.LAZY
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType.IDENTITY
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "events")
data class EventEntity(
    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(strategy = IDENTITY, generator = "uuid2")
    @Column(length = 36, nullable = false, updatable = false)
    val id: UUID?,
    val name: String,
    val creator: String,
    val created: Timestamp,
    @Column(name = "created_offset")
    val offset: String,
    val defaultCurrency: String,
    @OneToMany(mappedBy = "event", fetch = LAZY)
    val payments: List<PaymentEntity>,
)
