package com.Backend.Server.controller.dto

import com.Backend.Server.model.Candle

data class CandleResponse(
    val data: List<Candle>,
    val nextCursor: String?,
)
