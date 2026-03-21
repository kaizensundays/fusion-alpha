package com.kaizensundays.flights.service

import com.kaizensundays.flights.service.Flights.adjustFormat
import com.kaizensundays.flights.service.Flights.format
import com.kaizensundays.flights.service.messages.FindFlight
import org.junit.jupiter.api.Test
import java.time.LocalDate
import kotlin.test.assertEquals

/**
 * Created: Sunday 10/31/2021, 1:07 PM Eastern Time
 *
 * @author Sergey Chuykov & Junie (gpt-5-2025-08-07)
 */
class FindFlightConverterTest : MuTestSupport() {

    @Test
    fun test() {

        val requests = mapOf(
            "requests" to arrayOf(
                FindFlight(
                    "?", "localhost", "LAX", "LAS",
                    LocalDate.of(2021, 10, 11).format(),
                    LocalDate.of(2021, 10, 17).format(),
                    "7de066d6-f526-4c07-a1b2-21c50aaa9c60"
                ),
                FindFlight(
                    "?", "localhost", "LAX", "PHX",
                    LocalDate.of(2021, 10, 11).format(),
                    LocalDate.of(2021, 10, 17).format(),
                    "25029ccd-4034-467e-8aac-d4149d0f83b2"
                ),
                FindFlight(
                    "?", "localhost", "LAX", "SMF",
                    LocalDate.of(2021, 10, 11).format(),
                    LocalDate.of(2021, 10, 17).format(),
                    "4b06d7b1-d228-4b05-aa61-0e935f2ccf5f"
                )
            )
        )

        var wire = converter.fromObjects(requests)

        wire = wire.adjustFormat()

        wire = unixNL(wire)

        var json = Flights.read("/find-flights.json")

        json = unixNL(json)

        assertEquals(json, wire)

        val map = converter.toObjects(wire, Flights.findFlightTypeRef)

        assertEquals(3, map.mergeValues().size)
    }

    // [user] -> (FindFlight) <-- List<FlightOption>

}