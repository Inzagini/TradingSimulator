package com.Backend.Server.service

import com.Backend.Server.model.Candle
import com.Backend.Server.repository.CandleRepository
import com.Backend.Server.service.dto.JSONData
import kotlinx.serialization.json.Json
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import java.io.File
import java.time.Instant

@Service
class JSONMarketDataService(
    val candleRepository: CandleRepository,
) {
    fun fetchAndIngestData(): String {
        val content = File("testData.json").readText()

        val parsed =
            Json {
                ignoreUnknownKeys = true
            }.decodeFromString<JSONData>(content)

        val result = parsed.chart.result.first()
        val quote = result.indicators.quote.first()

        val candles =
            List(result.timestamp.size) { index ->
                Candle(
                    symbol = result.meta.symbol,
                    timestamp = Instant.ofEpochSecond(result.timestamp[index]),
                    open = quote.open[index] ?: 0.0,
                    close = quote.close[index] ?: 0.0,
                    high = quote.high[index] ?: 0.0,
                    low = quote.low[index] ?: 0.0,
                    volume = quote.volume[index] ?: 0L,
                )
            }

        candles.forEach { candle ->
            try {
                candleRepository.save(candle)
            } catch (e: DataIntegrityViolationException) {
            }
        }

        return "Readed data and saved to Database - test"
    }
}
