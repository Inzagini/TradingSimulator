package com.Backend.Server.controller

import com.Backend.Server.controller.dto.CandleResponse
import com.Backend.Server.service.CandleService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/candles")
class CandleControler(
    private val candleService: CandleService,
) {
    @GetMapping
    fun getCandles(
        @RequestParam symbol: String,
        @RequestParam(required = false) after: String?,
        @RequestParam(defaultValue = "100") limit: Int,
    ): CandleResponse = candleService.getCandlesCursor(symbol, after, limit)
}
