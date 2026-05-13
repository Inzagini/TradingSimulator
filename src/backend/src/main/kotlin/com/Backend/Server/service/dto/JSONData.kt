package com.Backend.Server.service.dto

import kotlinx.serialization.Serializable

@Serializable
data class JSONData(
    val chart: Chart,
)

@Serializable
data class Chart(
    val result: List<Result>,
)

@Serializable
data class Meta(
    val symbol: String
)

@Serializable
data class Result(
    val meta: Meta,
    val timestamp: List<Long>,
    val indicators: Indicator,
)

@Serializable
data class Indicator(
    val quote: List<Quote>,
)

@Serializable
data class Quote(
    val open: List<Double?>,
    val high: List<Double?>,
    val low: List<Double?>,
    val close: List<Double?>,
    val volume: List<Long?>,
)
