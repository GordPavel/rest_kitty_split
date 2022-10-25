package ru.sau.kitty_split.split.service

import org.springframework.stereotype.Service
import ru.sau.kitty_split.event.EventNotFoundException
import ru.sau.kitty_split.event.service.EventsService
import java.util.UUID

@Service
class KittySplitService(
    private val eventService: EventsService,
    private val splitService: SplitService,
    private val splitServiceMapper: KittySplitServiceMapper,
) {

    fun splitEvent(eventId: UUID): List<Transaction> {
        val (_, _, _, eventCurrency, _, payments, participants) = eventService.getEvent(eventId)
            ?: throw EventNotFoundException(eventId)

        val inputData = splitServiceMapper.mapPaymentsToInputData(payments, participants)
        val (_, transactions) = splitService.splitBill(inputData)

        return splitServiceMapper.mapOutputDataToTransactions(transactions, eventCurrency)
    }

}
