package com.kaizensundays.flights.service.booking

/**
 * Created: Monday 12/22/2025, 9:54 PM Eastern Time
 *
 * @author Sergey Chuykov & Junie (gpt-5-2025-08-07)
 */

// <<boundary>>
data class BookingResult(
    val confirmedPrice: Double,
    val status: BookingStatus
)

enum class BookingStatus {
    SUCCESS,
    EXPIRED_QUOTE_NEW_PRICE_APPLIED,
    REJECTED_NO_SEATS
}
