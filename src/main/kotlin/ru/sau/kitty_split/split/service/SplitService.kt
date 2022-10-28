package ru.sau.kitty_split.split.service

import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Service

interface SplitService {
    fun splitBill(inputData: InputData): OutputData
}

@Primary
@Service
class SplitServiceStub : SplitService {
    override fun splitBill(inputData: InputData): OutputData {
        TODO("Not yet implemented")
    }
}