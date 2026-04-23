package com.Backend.Server.service

import com.Backend.Server.controller.dto.CandleResponse
import com.Backend.Server.model.Candle
import com.Backend.Server.repository.CandleRepository
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class CandleService(
    private val candleRepository: CandleRepository,
) {
    fun getCandlesCursor(
        symbol: String,
        after: String?,
        limit: Int,
    ): CandleResponse {
        val startCursor = after?.let { Instant.parse(it) } ?: Instant.EPOCH

        val candles = candleRepository.findAfter(symbol, startCursor).take(limit)

        val nextCursor = candles.lastOrNull()?.timestamp?.toString()

        return CandleResponse(
            data = candles,
            nextCursor = nextCursor,
        )
    }

    private fun parseTime(value: String): Instant =
        try {
            Instant.parse(value)
        } catch (e: Exception) {
            throw IllegalArgumentException("Invalid timestamp format: $value")
        }
}
