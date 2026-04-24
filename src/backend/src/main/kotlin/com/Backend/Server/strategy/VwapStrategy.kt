package com.Backend.Server.strategy

import com.Backend.Server.model.Candle
import com.Backend.Server.service.IndicatorService
import com.Backend.Server.strategy.Signal
import com.Backend.Server.strategy.Strategy

class VwapStrategy(
    private val indicatorService: IndicatorService,
    private val window: Int,
    private val threshold: Double,
) : Strategy {
    override fun generateSignal(
        current: Candle,
        history: List<Candle>,
    ): Signal {
        val recent = history.takeLast(window)
        val vwap = indicatorService.calculateVWAP(recent) ?: return Signal.HOLD

        return when {
            current.close > vwap + threshold -> Signal.BUY
            current.close < vwap + threshold -> Signal.SELL
            else -> Signal.HOLD
        }
    }
}
