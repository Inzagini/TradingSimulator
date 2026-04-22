package com.Backend.Server.model

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(
    name = "candles",
    indexes = [
        Index(name = "idx_symbol_time", columnList = "symbol, timestamp"),
    ],
)
data class Candle(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @Column(nullable = false)
    val symbol: String,
    @Column(nullable = false)
    val timestamp: Instant,
    @Column(nullable = false)
    val open: Double,
    @Column(nullable = false)
    val close: Double,
    @Column(nullable = false)
    val high: Double,
    @Column(nullable = false)
    val low: Double,
    @Column(nullable = false)
    val volume: Long,
)
