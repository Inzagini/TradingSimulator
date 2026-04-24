package com.Backend.Server.strategy

enum class StrategyType {
    VWAP,
}

data class StrategyConfig(
    val type: StrategyType,
    val window: Int? = null,
    val threshold: Double? = null,
)
