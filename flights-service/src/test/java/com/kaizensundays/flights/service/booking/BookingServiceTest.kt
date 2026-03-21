package com.kaizensundays.flights.service.booking

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.Clock
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

/**
 * Created: Monday 12/22/2025, 9:54 PM Eastern Time
 *
 * @author Sergey Chuykov & Junie (gpt-5-2025-08-07)
 */

class BookingServiceTest {

    private class FakeFlightCache(private val flights: Map<String, Flight>) : FlightCache {
        override fun get(flightId: String): Flight? = flights[flightId]
    }

    private class FakeQuoteCache(private val quotes: Map<String, PriceQuote>) : QuoteCache {
        override fun get(quoteId: String): PriceQuote? = quotes[quoteId]
    }

    private val zone: ZoneId = ZoneOffset.UTC
    private val now: LocalDateTime = LocalDateTime.of(2025, 12, 22, 21, 54)
    private val clock: Clock = Clock.fixed(now.toInstant(ZoneOffset.UTC), zone)

    @Nested
    inner class AvailabilityChecks {
        @Test
        fun `reject when no seats`() {
            val flight = Flight("F1", basePrice = 100.0, availableSeats = 1, departureTime = now.plusDays(2))
            val service = BookingService(FakeFlightCache(mapOf("F1" to flight)), FakeQuoteCache(emptyMap()), clock)

            val result = service.book(BookingRequest("F1", passengerCount = 2, quoteId = null))

            assertEquals(BookingStatus.REJECTED_NO_SEATS, result.status)
            assertEquals(0.0, result.confirmedPrice)
        }
    }

    @Nested
    inner class QuoteValidation {
        @Test
        fun `success when valid quote not expired and within variance`() {
            val flight = Flight("F2", basePrice = 200.0, availableSeats = 5, departureTime = now.plusDays(3))
            val quote = PriceQuote("Q1", quotedPrice = 210.0, expirationTime = now.plusMinutes(10))
            val service = BookingService(FakeFlightCache(mapOf("F2" to flight)), FakeQuoteCache(mapOf("Q1" to quote)), clock)

            val result = service.book(BookingRequest("F2", passengerCount = 2, quoteId = "Q1"))

            assertEquals(BookingStatus.SUCCESS, result.status)
            assertEquals(420.0, result.confirmedPrice)
        }

        @Test
        fun `expired quote triggers new price without surcharge when departure after 24h`() {
            val flight = Flight("F3", basePrice = 150.0, availableSeats = 3, departureTime = now.plusDays(2))
            val quote = PriceQuote("Q2", quotedPrice = 100.0, expirationTime = now.minusMinutes(1))
            val service = BookingService(FakeFlightCache(mapOf("F3" to flight)), FakeQuoteCache(mapOf("Q2" to quote)), clock)

            val result = service.book(BookingRequest("F3", passengerCount = 2, quoteId = "Q2"))

            assertEquals(BookingStatus.EXPIRED_QUOTE_NEW_PRICE_APPLIED, result.status)
            // No surcharge (outside 24h), total = base * passengers
            assertEquals(300.0, result.confirmedPrice)
        }

        @Test
        fun `quote outside variance triggers recalculation and marks new price`() {
            val flight = Flight("F4", basePrice = 100.0, availableSeats = 4, departureTime = now.plusDays(2))
            // 300 is +200% from base, out of 50% variance
            val quote = PriceQuote("Q3", quotedPrice = 300.0, expirationTime = now.plusMinutes(5))
            val service = BookingService(FakeFlightCache(mapOf("F4" to flight)), FakeQuoteCache(mapOf("Q3" to quote)), clock)

            val result = service.book(BookingRequest("F4", passengerCount = 1, quoteId = "Q3"))

            assertEquals(BookingStatus.EXPIRED_QUOTE_NEW_PRICE_APPLIED, result.status)
            // Recalculated without surcharge since >24h -> base price, also capped within variance anyway
            assertEquals(100.0, result.confirmedPrice)
        }
    }

    @Nested
    inner class LastMinuteSurcharge {
        @Test
        fun `apply surcharge when within 24 hours`() {
            val flight = Flight("F5", basePrice = 250.0, availableSeats = 10, departureTime = now.plusHours(12))
            val service = BookingService(FakeFlightCache(mapOf("F5" to flight)), FakeQuoteCache(emptyMap()), clock)

            val result = service.book(BookingRequest("F5", passengerCount = 2, quoteId = null))

            assertEquals(BookingStatus.EXPIRED_QUOTE_NEW_PRICE_APPLIED, result.status)
            // 250 * 1.2 = 300 per seat; total for 2 = 600
            assertEquals(600.0, result.confirmedPrice)
        }

        @Test
        fun `no surcharge when outside 24 hours`() {
            val flight = Flight("F6", basePrice = 80.0, availableSeats = 10, departureTime = now.plusDays(2))
            val service = BookingService(FakeFlightCache(mapOf("F6" to flight)), FakeQuoteCache(emptyMap()), clock)

            val result = service.book(BookingRequest("F6", passengerCount = 3, quoteId = null))

            assertEquals(BookingStatus.EXPIRED_QUOTE_NEW_PRICE_APPLIED, result.status)
            assertEquals(240.0, result.confirmedPrice)
        }
    }

    @Nested
    inner class Validation {
        @Test
        fun `passenger count must be positive`() {
            val flight = Flight("F7", basePrice = 50.0, availableSeats = 10, departureTime = now.plusDays(1))
            val service = BookingService(FakeFlightCache(mapOf("F7" to flight)), FakeQuoteCache(emptyMap()), clock)

            assertThrows(IllegalArgumentException::class.java) {
                service.book(BookingRequest("F7", passengerCount = 0, quoteId = null))
            }
        }
    }
}
