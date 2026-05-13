package com.Backend.Server.controller.dto

import com.Backend.Server.model.Candle
import com.Backend.Server.service.dto.IndicatorPoint

data class CandleResponse(
    val data: List<Candle>,
    val nextCursor: String?,
    val vwap: Double? = null,
    val indicator: Map<String, List<IndicatorPoint>>? = null,
)
