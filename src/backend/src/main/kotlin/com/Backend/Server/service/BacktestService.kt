package com.Backend.Server.service

import com.Backend.Server.model.Candle
import com.Backend.Server.strategy.Signal
import com.Backend.Server.strategy.Strategy
import org.springframework.stereotype.Service

@Service
class BacktestService {
    fun runBacktest(
        candles: List<Candle>,
        strategy: Strategy,
    ): BacktestResult {
        var position = 0
        var entryPrice = 0.0
        var pnl = 0.0

        for (i in candles.indices) {
            val current = candles[i]
            val history = candles.subList(0, i + 1)

            val signal = strategy.generateSignal(current, history)
            println("signal=$signal bought=$position entry=$entryPrice pnl=$pnl")

            when (signal) {
                Signal.BUY -> {
                    if (position == 0) {
                        position = 1
                        entryPrice = current.close
                    }
                }

                Signal.SELL -> {
                    if (position == 1) {
                        pnl += current.close - entryPrice
                        position = 0
                    }
                }

                Signal.HOLD -> {} // skip
            }
        }

        return BacktestResult(
            totalPnl = pnl,
        )
    }
}
