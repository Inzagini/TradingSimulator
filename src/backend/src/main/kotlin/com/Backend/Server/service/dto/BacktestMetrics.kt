package com.Backend.Server.service.dto

data class BacktestMetrics(
    val totalTrades: Int,
    val winRate: Double,
    val avarageWin: Double,
    val avarageLoss: Double,
    val maxDrawdown: Double,
)
