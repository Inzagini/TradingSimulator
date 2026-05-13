package com.Backend.Server.service.dto

import java.time.Instant

data class IndicatorPoint(
    val timestamp: Instant,
    val value: Double,
)
