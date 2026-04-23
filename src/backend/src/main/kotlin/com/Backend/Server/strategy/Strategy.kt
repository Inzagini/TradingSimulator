package com.Backend.Server.strategy

import com.Backend.Server.model.Candle
import com.Backend.Server.strategy.Signal

interface Strategy {
    fun generateSignal(
        current: Candle,
        history: List<Candle>,
    ): Signal
}
