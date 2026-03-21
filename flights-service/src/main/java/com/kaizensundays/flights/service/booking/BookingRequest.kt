package com.kaizensundays.flights.service.booking

/**
 * Created: Monday 12/22/2025, 9:54 PM Eastern Time
 *
 * @author Sergey Chuykov & Junie (gpt-5-2025-08-07)
 */

// <<boundary>>
data class BookingRequest(
    val flightId: String,
    val passengerCount: Int,
    val quoteId: String?
)
