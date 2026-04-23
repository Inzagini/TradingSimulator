package com.Backend.Server.service

import com.Backend.Server.model.Candle
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
}
