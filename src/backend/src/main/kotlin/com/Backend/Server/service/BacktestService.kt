package com.Backend.Server.service

import com.Backend.Server.model.Candle
import com.Backend.Server.model.Trade
import com.Backend.Server.strategy.Signal
import com.Backend.Server.strategy.Strategy
import org.springframework.stereotype.Service

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

        var totalPnl = trades.sumOf { it.pnl() }

        return BacktestResult(
            totalPnl = totalPnl,
            trades = trades,
        )
    }
}
