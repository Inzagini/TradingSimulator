package com.Backend.Server.controller

import com.Backend.Server.service.JSONMarketDataService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/fetch/json")
class JsonIngestionControler(
    private val jsonService: JSONMarketDataService,
) {
    @GetMapping
    fun ingestJSON(): Any =
        try {
            jsonService.fetchAndIngestData()
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
}
