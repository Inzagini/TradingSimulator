package com.Backend.Server.repository

import com.Backend.Server.model.Candle
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.Instant

@Repository
interface CandleRepository : JpaRepository<Candle, Long> {
    @Query(
        """
        SELECT c FROM Candle c
        WHERE c.symbol = :symbol
        AND c.timestamp > :after 
        ORDER BY c.timestamp ASC
    """,
    )
    fun findAfter(
        @Param("symbol") symbol: String,
        @Param("after") start: Instant,
    ): List<Candle>

    @Query(
        """
        SELECT c FROM Candle c
        WHERE c.symbol = :symbol
        AND c.timestamp BETWEEN :start AND :end
        ORDER BY c.timestamp ASC
    """,
    )
    fun findCandles(
        @Param("symbol") symbol: String,
        @Param("start") start: Instant,
        @Param("end") end: Instant,
    ): List<Candle>
}
