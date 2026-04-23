package com.Backend.Server.controller

import com.Backend.Server.service.MarketDataIngestionService
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/ingest")
class IngestionController(
    private val ingestionService: MarketDataIngestionService,
) {
    @PostMapping("/{symbol}")
    fun ingest(
        @PathVariable symbol: String,
    ): String {
        ingestionService.ingestBatch(symbol)
        return "Ingestion completed for $symbol"
    }
}
