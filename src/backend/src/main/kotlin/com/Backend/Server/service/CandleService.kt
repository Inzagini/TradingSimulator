package com.Backend.Server.service

import com.Backend.Server.model.Candle
import com.Backend.Server.repository.CandleRepository
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class CandleService(
    private val candleRepository: CandleRepository,
) {
    fun getCandles(
        symbol: String,
        start: String,
        end: String,
    ): List<Candle> {
        val startTime = parseTime(start)
        val endTime = parseTime(end)
        return candleRepository.findBySymbolAndTimestampBetween(symbol, startTime, endTime)
    }

    private fun parseTime(value: String): Instant =
        try {
            Instant.parse(value)
        } catch (e: Exception) {
            throw IllegalArgumentException("Invalid timestamp format: $value")
        }
}
