package com.Backend.Server.strategy

import com.Backend.Server.model.Candle
import com.Backend.Server.service.IndicatorService
import com.Backend.Server.strategy.Signal
import com.Backend.Server.strategy.Strategy

class VwapStrategy(
    private val indicatorService: IndicatorService,
) : Strategy {
    override fun generateSignal(
        current: Candle,
        history: List<Candle>,
    ): Signal {
        val vwap = indicatorService.calculateVWAP(history) ?: return Signal.HOLD

        return when {
            current.close > vwap -> Signal.BUY
            current.close < vwap -> Signal.SELL
            else -> Signal.HOLD
        }
    }
}
