package com.Backend.Server.service

import com.Backend.Server.model.Candle
import com.Backend.Server.repository.CandleRepository
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import java.time.Instant

@Service
class MarketDataIngestionService(
    private val candleRepository: CandleRepository,
) {
    private val client = WebClient.create()

    fun ingestDummyData(symbol: String) {
        val candles = listOf(
            Candle(
                symbol = symbol,
                timestamp = Instant.parse("2026-01-01T10:00:00Z"),
                open = 4800.0,
                high = 4810.0,
                low = 4795.0,
                close = 4805.0,
                volume = 1200
            ),
            Candle(
                symbol = symbol,
                timestamp = Instant.parse("2026-01-01T10:01:00Z"),
                open = 4805.0,
                high = 4820.0,
                low = 4800.0,
                close = 4815.0,
                volume = 900
            )
        )

        candleRepository.saveAll(candles)
    }
}
