package com.Backend.Server.service

import com.Backend.Server.model.Candle
import com.Backend.Server.model.Trade
import com.Backend.Server.strategy.Signal
import com.Backend.Server.strategy.Strategy
import org.springframework.stereotype.Service
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
        val metrics = calculateMetric(trades)

        return BacktestResult(
            totalPnl = totalPnl,
            trades = trades,
            metrics = metrics,
        )
    }

    private fun calculateMetric(trades: List<Trade>): BacktestMetrics {
        if (trades.isEmpty()) {
            return BacktestMetrics(0, 0.0, 0.0, 0.0, 0.0)
        }

        val plns = trades.map { it.pnl() }

        val wins = plns.filter { it > 0 }
        val losses = plns.filter { it < 0 }

        val totalTrades = trades.size
        val winRate = wins.size.toDouble() / totalTrades
        val avgWin = if (wins.isEmpty()) 0.0 else wins.average()
        val avgLoss = if (losses.isEmpty()) 0.0 else losses.average()

        var peak = 0.0
        var equity = 0.0
        var maxDrawdown = 0.0

        for (pnl in plns) {
            equity += pnl
            peak = max(peak, equity)
            val drawdown = equity - peak
            maxDrawdown = minOf(maxDrawdown, drawdown)
        }

        return BacktestMetrics(
            totalTrades = totalTrades,
            winRate = winRate,
            avarageWin = avgWin,
            avarageLoss = avgLoss,
            maxDrawdown = maxDrawdown,
        )
    }
}
