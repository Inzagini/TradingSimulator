package com.Backend.Server.controller

import com.Backend.Server.model.Candle
import com.Backend.Server.service.CandleService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.Instant
import java.time.format.DateTimeFormatter

@RestController
@RequestMapping("/candles")
class CandleControler(
    private val candleService: CandleService,
) {
    @GetMapping
    fun getCandles(
        @RequestParam symbol: String,
        @RequestParam start: String,
        @RequestParam end: String,
        @RequestParam(defaultValue = "1000") limit: Int,
        @RequestParam(defaultValue = "0") offset: Int,
    ): List<Candle> = candleService.getCandles(symbol, start, end, limit, offset)
}
