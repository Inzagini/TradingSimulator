package com.Backend.Server.controller

import com.Backend.Server.controller.dto.BacktestSweepRequest
import com.Backend.Server.repository.CandleRepository
import com.Backend.Server.service.BacktestService
import com.Backend.Server.service.IndicatorService
import com.Backend.Server.strategy.StrategyFactory
import com.Backend.Server.strategy.VwapStrategy
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.Instant

@RestController
@RequestMapping("/backtest")
class BacktestController(
    private val candleRepository: CandleRepository,
    private val backtestService: BacktestService,
    private val strategyFactory: StrategyFactory,
) {
    @GetMapping
    fun runBacktest(
        @RequestParam symbol: String,
        @RequestParam start: String,
        @RequestParam end: String,
        @RequestParam window: Int,
        @RequestParam threshold: Double,
    ): Any {
        val candles = candleRepository.findCandles(symbol, Instant.parse(start), Instant.parse(end))
        val strategy = strategyFactory.createVwapStragy(window, threshold)

        return backtestService.runBacktest(candles, strategy)
    }

    @PostMapping("/sweep")
    fun runSweep(
        @RequestBody request: BacktestSweepRequest,
    ): Any {
        val candles =
            candleRepository.findCandles(
                request.symbol,
                Instant.parse(request.start),
                Instant.parse(request.end),
            )

        return backtestService.runParameterSweep(
            candles,
            request.windows,
            request.thresholds,
            strategyFactory,
        )
    }
}
