package com.Backend.Server.strategy

import com.Backend.Server.service.IndicatorService
import org.springframework.stereotype.Component

@Component
class StrategyFactory(
    private val indicatorService: IndicatorService,
) {
    fun createVwapStragy(
        window: Int,
        threshold: Double,
    ): Strategy = VwapStrategy(indicatorService, window, threshold)
}
