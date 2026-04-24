package com.Backend.Server.service.dto

import java.time.Instant

data class EquityPoint(
    val timestamp: Instant?,
    val equity: Double,
)
