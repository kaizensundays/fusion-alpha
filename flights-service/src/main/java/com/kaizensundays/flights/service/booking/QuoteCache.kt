package com.kaizensundays.flights.service.booking

/**
 * Created: Monday 12/22/2025, 9:54 PM Eastern Time
 *
 * @author Sergey Chuykov & Junie (gpt-5-2025-08-07)
 */

// <<boundary>>
fun interface QuoteCache {
    fun get(quoteId: String): PriceQuote?
}
