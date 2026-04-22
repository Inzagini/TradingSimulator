package com.Backend.Server.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HealthController {
    @GetMapping("health")
    fun healt(): Map<String, String> =
        mapOf(
            "statue" to "UP",
            "service" to "trading-simulator",
        )
}
