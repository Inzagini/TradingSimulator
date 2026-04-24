package com.Backend.Server.service

data class BacktestMetrics(
    val totalTrades: Int,
    val winRate: Double,
    val avarageWin: Double,
    val avarageLoss: Double,
    val maxDrawdown: Double,
)
