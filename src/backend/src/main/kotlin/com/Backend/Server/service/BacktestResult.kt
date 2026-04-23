package com.Backend.Server.service

import com.Backend.Server.model.Trade

data class BacktestResult(
    val totalPnl: Double,
    val trades: List<Trade>,
)
