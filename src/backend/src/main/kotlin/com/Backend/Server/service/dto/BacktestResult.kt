package com.Backend.Server.service.dto

import com.Backend.Server.model.Trade

data class BacktestResult(
    val totalPnl: Double,
    val trades: List<Trade>,
    val metrics: BacktestMetrics,
    val equityCurve: List<EquityPoint>,
)
