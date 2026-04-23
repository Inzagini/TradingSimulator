package com.Backend.Server.controller

import com.Backend.Server.service.CsvMarketDataService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/ingest/csv")
class CsvIngestionController(
    private val csvService: CsvMarketDataService,
) {
    @PostMapping
    fun ingestCsv(
        @RequestParam symbol: String,
        @RequestParam path: String,
    ): String {
        csvService.ingestFromCsv(symbol, path)
        return "CSV ingestion completed: $symbol"
    }
}
