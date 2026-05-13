package com.Backend.Server.service

import com.Backend.Server.model.Candle
import com.Backend.Server.service.dto.IndicatorPoint
import org.springframework.stereotype.Service

@Service
class IndicatorService {
    fun calculateVWAP(candles: List<Candle>): Double? {
        if (candles.isEmpty()) {
            return null
        }

        var cumulativePV = 0.0
        var cumulativeVolume = 0.0

        for (candle in candles) {
            val avrgPrice = (candle.high + candle.low + candle.close) / 3

            cumulativePV += avrgPrice * candle.volume
            cumulativeVolume += candle.volume
        }

        return if (cumulativeVolume == 0.0) null else cumulativePV / cumulativeVolume
    }

    fun calculateVWAPSeries(candles: List<Candle>): List<IndicatorPoint> {
        val result = mutableListOf<IndicatorPoint>()
        if (candles.isEmpty()) {
            return result
        }

        var cumulativePV = 0.0
        var cumulativeVolume = 0.0

        for (candle in candles) {
            val avgPrice = (candle.high + candle.low + candle.close) / 3

            cumulativePV += avgPrice * candle.volume
            cumulativeVolume += candle.volume

            if (cumulativeVolume == 0.0) {
                continue
            }

            val vwap = cumulativePV / cumulativeVolume

            result.add(
                IndicatorPoint(
                    timestamp = candle.timestamp,
                    value = vwap,
                ),
            )
        }

        return result
    }
}
