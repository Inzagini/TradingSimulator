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

    @Query(
        value = """
        SELECT
            min(c.id) AS id,
            max(c.symbol) AS symbol,
            bucket AS timestamp,
            (array_agg(c.open ORDER BY c.timestamp ASC))[1] AS open,
            max(c.high) AS high,
            min(c.low) AS low,
            (array_agg(c.close ORDER BY c.timestamp DESC))[1] AS close,
            SUM(c.volume) AS volume
        FROM (
            SELECT
                c.*,
                time_bucket(
                    CASE :interval
                        WHEN '1m' THEN INTERVAL '1 minute'
                        WHEN '5m' THEN INTERVAL '5 minutes'
                        WHEN '15m' THEN INTERVAL '15 minutes'
                        WHEN '1h' THEN INTERVAL '1 hour'
                        ELSE INTERVAL '5 minutes'
                    END,
                    c.timestamp
                ) AS bucket
            FROM candles c
            WHERE c.symbol = :symbol
              AND c.timestamp > :after
        ) c
        GROUP BY bucket
        ORDER BY bucket ASC
        """,
        nativeQuery = true,
    )
    fun findAfterv2(
        @Param("symbol") symbol: String,
        @Param("interval") interval: String,
        @Param("after") start: Instant,
    ): List<Candle>
}
