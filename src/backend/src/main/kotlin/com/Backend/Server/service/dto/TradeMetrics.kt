package com.Backend.Server.service.dto

data class TradeMetrics(
    val totalTrades: Int,
    val winRate: Double,
    val averageWin: Double,
    val averageLoss: Double,
)
