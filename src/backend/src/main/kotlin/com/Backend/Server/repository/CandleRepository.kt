package com.Backend.Server.repository

import com.Backend.Server.model.Candle
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.Instant

@Repository
interface CandleRepository : JpaRepository<Candle, Long> {
    fun findBySymbolAndTimestampBetween(
        symbol: String,
        start: Instant,
        end: Instant,
    ): List<Candle>
}
