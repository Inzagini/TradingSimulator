package com.Backend.Server.controller

import com.Backend.Server.controller.dto.MarketProfileResponse
import com.Backend.Server.repository.CandleRepository
import com.Backend.Server.service.MarketProfileService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.Instant

@RestController
@RequestMapping("/profile")
class MarketProfileController(
    private val candleRepository: CandleRepository,
    private val marketProfileService: MarketProfileService,
) {
    @GetMapping
    fun getProfile(
        @RequestParam symbol: String,
        @RequestParam start: String,
        @RequestParam end: String,
    ): MarketProfileResponse {
        val candles = candleRepository.findCandles(symbol, Instant.parse(start), Instant.parse(end))

        return marketProfileService.buildProfile(candles)
    }
}
