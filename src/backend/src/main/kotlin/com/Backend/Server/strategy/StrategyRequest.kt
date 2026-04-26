package com.Backend.Server.strategy

data class StrategyRequest(
    val type: String,
    val params: Map<String, Any>,
)
