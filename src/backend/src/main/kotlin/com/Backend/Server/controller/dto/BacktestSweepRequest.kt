package com.Backend.Server.controller.dto

data class BacktestSweepRequest(
    val symbol: String,
    val start: String,
    val end: String,
    val windows: List<Int>,
    val thresholds: List<Double>,
)
