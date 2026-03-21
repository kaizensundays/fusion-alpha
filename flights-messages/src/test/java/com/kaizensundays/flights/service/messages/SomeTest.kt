package com.kaizensundays.flights.service.messages

import com.kaizensundays.lab.grpc.DayOfWeek
import com.kaizensundays.lab.grpc.Flight
import com.kaizensundays.lab.grpc.FlightRoute
import com.kaizensundays.lab.grpc.FlightStatus
import com.kaizensundays.lab.grpc.Schedule
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.test.assertEquals

/**
 * Created: Saturday 5/31/2025, 1:14 PM Eastern Time
 *
 * @author Sergey Chuykov & Junie (gpt-5-2025-08-07)
 */
class SomeTest {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    val dtf = DateTimeFormatter.ISO_ZONED_DATE_TIME
        .withLocale(Locale.US)
        .withZone(ZoneId.of("America/Los_Angeles"))

    fun zonedDateTime(s: String): ZonedDateTime {
        return ZonedDateTime.parse(s, dtf)
    }

    @Test
    fun test() {

        val departureTime = zonedDateTime("2025-05-31T11:17:00-07:00[America/Los_Angeles]")
        val arrivalTime = zonedDateTime("2025-05-31T12:33:00-07:00[America/Los_Angeles]")

        val departure = dtf.format(departureTime)
        val arrival = dtf.format(arrivalTime)

        val flightRoute = FlightRoute.newBuilder()
            .setFlightNumber(1)
            .setAirlineId(1)
            .setDepartureAirport("SFO")
            .setArrivalAirport("LAS")
            .build()

        val schedule = Schedule.newBuilder()
            .setScheduleId(1)
            .setScheduledDeparture(departure)
            .setScheduledArrival(arrival)
            .addDaysOfOperation(DayOfWeek.MON)
            .addDaysOfOperation(DayOfWeek.WED)
            .addDaysOfOperation(DayOfWeek.FRI)
            .build()

        val flight = Flight.newBuilder()
            .setId(1)
            .setScheduleId(schedule.scheduleId)
            .setDeparture(departure)
            .setArrival(arrival)
            .setStatus(FlightStatus.SCHEDULED)
            .build()

        assertEquals(FlightStatus.SCHEDULED, flight.status)
    }

}