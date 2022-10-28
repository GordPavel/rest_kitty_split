package ru.sau.kitty_split.split.service

import org.springframework.stereotype.Service

interface SplitService {
    fun splitBill(inputData: InputData): OutputData
}

@Service
class SplitServiceStub : SplitService {
    override fun splitBill(inputData: InputData): OutputData {
        TODO("Not yet implemented")
    }
}