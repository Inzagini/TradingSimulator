package com.Backend.Server.controller.dto

data class MarketProfileResponse(
    val valueAreaHigh: Double,
    val valueAreaLow: Double,
    val pointOfControl: Double,
    val distribution: Map<Double, Long>,
)
