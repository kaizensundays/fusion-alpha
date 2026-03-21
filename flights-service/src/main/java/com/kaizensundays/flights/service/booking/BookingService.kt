package com.kaizensundays.flights.service.booking

import java.time.Clock
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

/**
 * Created: Monday 12/22/2025, 9:54 PM Eastern Time
 *
 * @author Sergey Chuykov & Junie (gpt-5-2025-08-07)
 */

// <<control>>
class BookingService(
    private val flightCache: FlightCache,
    private val quoteCache: QuoteCache,
    private val clock: Clock
)
{
    // Configuration constants per requirements
    private val surchargeRate: Double = 0.20 // +20%
    private val lastMinuteWindow: Duration = Duration.ofHours(24)
    private val maxVarianceRate: Double = 0.50 // 50%

    fun book(request: BookingRequest): BookingResult {
        require(request.passengerCount > 0) { "passengerCount must be positive" }

        val flight = flightCache.get(request.flightId)
            ?: return BookingResult(confirmedPrice = 0.0, status = BookingStatus.REJECTED_NO_SEATS)

        // Availability Check
        if (flight.availableSeats < request.passengerCount) {
            return BookingResult(confirmedPrice = 0.0, status = BookingStatus.REJECTED_NO_SEATS)
        }

        val now = LocalDateTime.now(clock.withZone(ZoneId.of("UTC")))
        val perSeatPriceFromQuote: Double? = request.quoteId?.let { qid ->
            val q = quoteCache.get(qid)
            if (q != null && !isExpired(q.expirationTime, now)) q.quotedPrice else null
        }

        val base = flight.basePrice
        val lastMinute = isWithinLastMinuteWindow(flight.departureTime, now, lastMinuteWindow)

        val (perSeatPrice, usedQuote) = if (perSeatPriceFromQuote != null) {
            // Apply consistency check; if out of variance, disregard the quote and compute new price
            val withinVariance = withinVariance(perSeatPriceFromQuote, base, maxVarianceRate)
            if (withinVariance) perSeatPriceFromQuote to true else computeNewPerSeatPrice(base, lastMinute) to false
        } else {
            computeNewPerSeatPrice(base, lastMinute) to false
        }

        val adjustedPerSeat = capToVariance(perSeatPrice, base, maxVarianceRate)
        val totalPrice = roundMoney(adjustedPerSeat * request.passengerCount)

        val status = if (usedQuote) BookingStatus.SUCCESS else BookingStatus.EXPIRED_QUOTE_NEW_PRICE_APPLIED

        return BookingResult(confirmedPrice = totalPrice, status = status)
    }

    // Pure helper functions below

    private fun isExpired(expiration: LocalDateTime, now: LocalDateTime): Boolean = now.isAfter(expiration)

    private fun isWithinLastMinuteWindow(departure: LocalDateTime, now: LocalDateTime, window: Duration): Boolean {
        val threshold = now.plus(window)
        return !departure.isAfter(threshold) // departure <= now + window
    }

    private fun computeNewPerSeatPrice(basePrice: Double, lastMinute: Boolean): Double {
        return if (lastMinute) basePrice * (1.0 + surchargeRate) else basePrice
    }

    private fun withinVariance(price: Double, basePrice: Double, variance: Double): Boolean {
        if (basePrice <= 0.0) return true // avoid division by zero; treat as within
        val diffRate = abs(price - basePrice) / basePrice
        return diffRate <= variance
    }

    private fun capToVariance(price: Double, basePrice: Double, variance: Double): Double {
        if (basePrice <= 0.0) return price
        val lower = basePrice * (1.0 - variance)
        val upper = basePrice * (1.0 + variance)
        return min(max(price, lower), upper)
    }

    private fun roundMoney(value: Double): Double = kotlin.math.round(value * 100.0) / 100.0
}
