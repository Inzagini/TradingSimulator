package com.Backend.Server.service

import com.Backend.Server.model.Candle
import com.Backend.Server.repository.CandleRepository
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import java.io.BufferedReader
import java.io.FileReader
import java.time.Instant

@Service
class CsvMarketDataService(
    private val candleRepository: CandleRepository,
) {
    fun ingestFromCsv(
        symbol: String,
        filePath: String,
    ) {
        // TODO: need to safely check the file path
        BufferedReader(FileReader(filePath)).use { reader ->

            reader.readLine()

            reader.forEachLine { line ->

                val parts = line.split(",")

                val candle =
                    Candle(
                        symbol = symbol,
                        timestamp = Instant.parse(parts[0]),
                        open = parts[1].toDouble(),
                        high = parts[2].toDouble(),
                        low = parts[3].toDouble(),
                        close = parts[4].toDouble(),
                        volume = parts[5].toLong(),
                    )

                try {
                    candleRepository.save(candle)
                } catch (e: DataIntegrityViolationException) {
                }
            }
        }
    }
}
