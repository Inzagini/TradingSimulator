package com.Backend.Server.strategy

import com.Backend.Server.service.IndicatorService
import org.springframework.stereotype.Component

@Component
class StrategyFactory(
    private val indicatorService: IndicatorService,
) {
    fun create(config: StrategyConfig): Strategy =
        when (config.type) {
            StrategyType.VWAP -> createVWAP(config)
        }

    private fun createVWAP(config: StrategyConfig): Strategy {
        val window = config.window ?: throw IllegalArgumentException("VWAP require window")
        val threshold = config.threshold ?: throw IllegalArgumentException("VWAP require threshold")
        return VwapStrategy(indicatorService, window, threshold)
    }

    fun createVwapStragy(
        window: Int,
        threshold: Double,
    ): Strategy = VwapStrategy(indicatorService, window, threshold)
}
