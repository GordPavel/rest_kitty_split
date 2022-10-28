package ru.sau.kitty_split.split.controller

import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.sau.kitty_split.commons.ApiErrorResponse
import ru.sau.kitty_split.split.service.KittySplitService
import java.util.UUID

@RestController
@RequestMapping("/events/{eventId}/transactions")
class SplitController(
    private val kittySplitService: KittySplitService,
    private val splitControllerMapper: SplitControllerMapper,
) {

    @ApiOperation(value = "Получение списка транзакций для оплаты счета")
    @ApiResponses(
        ApiResponse(
            code = 200,
            response = TransactionDto::class,
            responseContainer = "List",
            message = "Список транзакций успешно получен",
        ),
        ApiResponse(code = 400, response = ApiErrorResponse::class, message = "Ошибка входных данных"),
        ApiResponse(code = 404, response = ApiErrorResponse::class, message = "Сущность не найдена"),
        ApiResponse(code = 500, response = ApiErrorResponse::class, message = "Внутренняя ошибка сервера"),
    )
    @GetMapping
    fun getEventTransactions(
        @ApiParam(
            name = "Id события",
            example = "576ac17b-c5b9-4bb7-a33f-317e38d03044",
            required = true,
            type = "java.util.UUID",
        )
        @PathVariable eventId: UUID
    ): List<TransactionDto> = kittySplitService.splitEvent(eventId)
        .let(splitControllerMapper::mapTransactions)

}
