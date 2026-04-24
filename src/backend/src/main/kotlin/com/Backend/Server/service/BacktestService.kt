package com.Backend.Server.service

import com.Backend.Server.model.Candle
import com.Backend.Server.model.Trade
import com.Backend.Server.service.dto.BacktestMetrics
import com.Backend.Server.service.dto.BacktestResult
import com.Backend.Server.service.dto.EquityPoint
import com.Backend.Server.service.dto.SweepResult
import com.Backend.Server.service.dto.TradeMetrics
import com.Backend.Server.strategy.Signal
import com.Backend.Server.strategy.Strategy
import com.Backend.Server.strategy.StrategyConfig
import com.Backend.Server.strategy.StrategyFactory
import com.Backend.Server.strategy.StrategyType
import org.springframework.stereotype.Service
import kotlin.Pair
import kotlin.math.max

@Service
class BacktestService {
    fun runBacktest(
        candles: List<Candle>,
        strategy: Strategy,
    ): BacktestResult {
        val trades = mutableListOf<Trade>()
        var openTrades: Trade? = null

        for (i in candles.indices) {
            val current = candles[i]
            val history = candles.subList(0, i + 1)

            val signal = strategy.generateSignal(current, history)

            println("Signal: $signal price: ${current.close}")

            when (signal) {
                Signal.BUY -> {
                    if (openTrades == null) {
                        openTrades =
                            Trade(
                                entryTime = current.timestamp,
                                entryPrice = current.close,
                            )
                    }
                }

                Signal.SELL -> {
                    if (openTrades != null) {
                        var closedTrade =
                            openTrades.copy(
                                exitTime = current.timestamp,
                                exitPrice = current.close,
                            )

                        trades.add(closedTrade)
                        openTrades = null
                    }
                }

                Signal.HOLD -> {} // skip
            }
        }

        val totalPnl = trades.sumOf { it.pnl() }
        val tradeMetrics = calculateMetric(trades)
        val equityCurve = buildMarkToMarketEquity(candles, trades)

        val tmp = calculateDrawdown(equityCurve)
        val maxDrawdown = tmp.first
        val maxDrawdownPercent = tmp.second

        val metrics =
            BacktestMetrics(
                totalTrades = tradeMetrics.totalTrades,
                winRate = tradeMetrics.winRate,
                averageWin = tradeMetrics.averageWin,
                averageLoss = tradeMetrics.averageLoss,
                maxDrawdown = maxDrawdown,
                maxDrawdownPercent = maxDrawdownPercent,
            )

        return BacktestResult(
            totalPnl = totalPnl,
            trades = trades,
            metrics = metrics,
            equityCurve = equityCurve,
        )
    }

    private fun calculateMetric(trades: List<Trade>): TradeMetrics {
        if (trades.isEmpty()) {
            return TradeMetrics(0, 0.0, 0.0, 0.0)
        }

        val plns = trades.map { it.pnl() }

        val wins = plns.filter { it > 0 }
        val losses = plns.filter { it < 0 }

        val totalTrades = trades.size
        val winRate = wins.size.toDouble() / totalTrades
        val avgWin = if (wins.isEmpty()) 0.0 else wins.average()
        val avgLoss = if (losses.isEmpty()) 0.0 else losses.average()

        return TradeMetrics(
            totalTrades = totalTrades,
            winRate = winRate,
            averageWin = avgWin,
            averageLoss = avgLoss,
        )
    }

    fun runParameterSweep(
        candles: List<Candle>,
        windows: List<Int>,
        thresholds: List<Double>,
        strategyFactory: StrategyFactory,
    ): List<SweepResult> {
        val results = mutableListOf<SweepResult>()

        for (window in windows) {
            for (threshold in thresholds) {
                val config =
                    StrategyConfig(
                        type = StrategyType.VWAP,
                        window = window,
                        threshold = threshold,
                    )
                val strategy = strategyFactory.create(config)

                val result = runBacktest(candles, strategy)

                results.add(SweepResult(window, threshold, result))
            }
        }

        return results.sortedByDescending { it.result.totalPnl }
    }

    private fun buildEquityCurve(trades: List<Trade>): List<EquityPoint> {
        val curve = mutableListOf<EquityPoint>()

        var equity = 0.0

        for (trade in trades) {
            equity += trade.pnl()
            curve.add(EquityPoint(timestamp = trade.exitTime, equity = equity))
        }

        return curve
    }

    private fun buildMarkToMarketEquity(
        candles: List<Candle>,
        trades: List<Trade>,
    ): List<EquityPoint> {
        val curve = mutableListOf<EquityPoint>()

        val initCapital = 10_000.0

        var realizePnl = 0.0
        var currentTrade: Trade? = null
        var tradeIndex = 0

        for (candle in candles) {
            if (tradeIndex < trades.size && trades[tradeIndex].entryTime == candle.timestamp) {
                currentTrade = trades[tradeIndex]
            }

            if (currentTrade != null && currentTrade.exitTime == candle.timestamp) {
                realizePnl += currentTrade.pnl()
                currentTrade = null
                tradeIndex++
            }

            val unrealizePnl =
                if (currentTrade != null) (candle.close - currentTrade.entryPrice) * currentTrade.quantity else 0.0

            val equity = initCapital + realizePnl + unrealizePnl

            curve.add(EquityPoint(candle.timestamp, equity))
        }

        return curve
    }

    private fun calculateDrawdown(equityCurve: List<EquityPoint>): Pair<Double, Double> {
        var peak = equityCurve.first().equity
        var maxDrawdown = 0.0
        var maxDrawdownPercent = 0.0

        for (point in equityCurve) {
            if (point.equity > peak) {
                peak = point.equity
            }

            val drawdown = point.equity - peak

            if (drawdown < maxDrawdown) {
                maxDrawdown = drawdown

                if (peak != 0.0) {
                    maxDrawdownPercent = drawdown / peak
                }
            }
        }

        return Pair(maxDrawdown, maxDrawdownPercent)
    }
}
