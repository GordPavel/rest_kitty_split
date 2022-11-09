package ru.sau.kitty_split.split.service

import kotlin.*
import org.springframework.stereotype.Service
import java.math.BigDecimal

interface SplitService {
    fun splitBill(inputData: InputData): OutputData
}

@Service
class SplitServiceStub : SplitService {
    override fun splitBill(inputData: InputData): OutputData {
        val participants = inputData.participants
        val transactions = mutableMapOf<String, Map<String, BigDecimal>>()

        for(p in participants){
            transactions[p] = mutableMapOf<String, BigDecimal>()
        }

        for(a in inputData.spendings){
            for((index, value) in a.parts.withIndex()){
                if(a.payer != participants[index]){
                    if(a.payer in transactions[participants[index]]){
                        transactions[participants[index]][a.payer].add(value)
                    }else transactions[participants[index]][a.payer] = value
                }

            }
        }

        for(payer in participants){
            val toDel = mutableListOf<String>()
            for((key, value) in transactions[payer]){
                if (payer in transactions[key]){
                    if(value > transactions[key][payer]){
                        value.substract(transactions[key][payer])
                        transactions[key].remove(payer)
                    }else{
                        transactions[key][payer].substract(value)
                        toDel.add(key)
                    }
                }
            }

            for(key in toDel){
                transactions[payer].remove(key)
            }
        }

        return OutputData(participants, transactions)
    }
}