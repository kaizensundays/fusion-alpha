package com.kaizensundays.flights.service.booking

import java.time.LocalDateTime

/**
 * Created: Monday 12/22/2025, 9:54 PM Eastern Time
 *
 * @author Sergey Chuykov & Junie (gpt-5-2025-08-07)
 */

// <<entity>>
data class PriceQuote(
    val quoteId: String,
    val quotedPrice: Double, // per-seat price
    val expirationTime: LocalDateTime
)
