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
        start: Instant,
        end: Instant,
    ): List<Candle> = candleRepository.findBySymbolAndTimestampBetween(symbol, start, end)
}
