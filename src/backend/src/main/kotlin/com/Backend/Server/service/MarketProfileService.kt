package com.Backend.Server.service

import com.Backend.Server.controller.dto.MarketProfileResponse
import com.Backend.Server.model.Candle
import org.springframework.stereotype.Service
import kotlin.math.round

@Service
class MarketProfileService {
    fun buildProfile(candles: List<Candle>): MarketProfileResponse {
        val distribution = mutableMapOf<Double, Long>()

        for (candle in candles) {
            val avgPrice = (candle.high + candle.low + candle.close) / 3

            val bucket = round(avgPrice / 5.0) * 5.0

            distribution[bucket] = distribution.getOrDefault(bucket, 0L) + candle.volume
        }

        val poc = distribution.maxBy { it.value }.key

        val totalVolume = distribution.values.sum()
        val sorted = distribution.entries.sortedByDescending { it.value }

        var cumulative = 0L
        val valueAreaBucket = mutableListOf<Double>()

        for (entry in sorted) {
            cumulative += entry.value
            valueAreaBucket.add(entry.key)

            if (cumulative >= totalVolume * 0.7) break
        }

        val valH = valueAreaBucket.max()
        val valL = valueAreaBucket.min()

        return MarketProfileResponse(
            valueAreaHigh = valH,
            valueAreaLow = valL,
            pointOfControl = poc,
            distribution = distribution,
        )
    }
}
