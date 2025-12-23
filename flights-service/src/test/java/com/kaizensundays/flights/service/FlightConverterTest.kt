package com.kaizensundays.flights.service

import com.kaizensundays.flights.service.Flights.adjustFormat
import com.kaizensundays.flights.service.Flights.flightTypeRef
import com.kaizensundays.flights.service.Flights.read
import com.kaizensundays.flights.service.messages.Flight
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/**
 * Created: Saturday 10/30/2021, 1:30 PM Eastern Time
 *
 * @author Sergey Chuykov & Junie (gpt-5-2025-08-07)
 */
class FlightConverterTest : MuTestSupport() {

    @Test
    fun fromObjects() {

        val flights = mapOf(
            "LAX-LAS" to arrayOf(
                Flight("AA2002", "AA", 128.0, 715, 826),
                Flight("UA2125", "UA", 133.0, 820, 937)
            ),
            "LAX-PHX" to arrayOf(
                Flight("AA767", "AA", 177.0, 1152, 1418),
                Flight("UA5411", "UA", 182.0, 800, 1027)
            )
        )

        var wire = converter.fromObjects(flights)

        wire = wire.adjustFormat()

        wire = unixNL(wire)

        var json = read("/flights.json")

        json = unixNL(json)

        assertEquals(json, wire)

        val map = converter.toObjects(wire, flightTypeRef)

        assertEquals(2, map.size)
    }

}