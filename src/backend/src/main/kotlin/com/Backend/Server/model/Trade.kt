package com.Backend.Server.model

import java.time.Instant

data class Trade(
    val entryTime: Instant,
    val exitTime: Instant? = null,
    val entryPrice: Double,
    val exitPrice: Double? = null,
    val quantity: Int = 1,
) {
    fun pnl(): Double {
        if (exitPrice == null) {
            return 0.0
        }
        return (exitPrice - entryPrice) * quantity
    }
}
