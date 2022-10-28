package ru.sau.kitty_split.split.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.sau.kitty_split.split.service.KittySplitService
import java.util.UUID

@RestController
@RequestMapping("/events/{eventId}/transactions")
class SplitController(
    private val kittySplitService: KittySplitService,
    private val splitControllerMapper: SplitControllerMapper,
) {

    @GetMapping
    fun getEventTransactions(
        @PathVariable eventId: UUID
    ): List<TransactionDto> = kittySplitService.splitEvent(eventId)
        .let(splitControllerMapper::mapTransactions)

}
