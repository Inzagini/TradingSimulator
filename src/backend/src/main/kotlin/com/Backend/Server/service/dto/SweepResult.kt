package com.Backend.Server.service.dto

data class SweepResult(
    val window: Int,
    val threshold: Double,
    val result: BacktestResult,
)
