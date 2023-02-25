package com.kaizensundays.particles.fusion.mu

import com.kaizensundays.particles.fusion.mu.Flights.adjustFormat
import com.kaizensundays.particles.fusion.mu.messages.FindFlight
import org.junit.Test
import java.time.LocalDate
import kotlin.test.assertEquals

/**
 * Created: Sunday 10/31/2021, 1:07 PM Eastern Time
 *
 * @author Sergey Chuykov
 */
class FindFlightConverterTest : MuTestSupport() {

    @Test
    fun test() {

        val requests = mapOf(
            "requests" to arrayOf(
                FindFlight(
                    "?", "localhost", "LAX", "LAS",
                    LocalDate.of(2021, 10, 11),
                    LocalDate.of(2021, 10, 17),
                    "7de066d6-f526-4c07-a1b2-21c50aaa9c60"
                ),
                FindFlight(
                    "?", "localhost", "LAX", "PHX",
                    LocalDate.of(2021, 10, 11),
                    LocalDate.of(2021, 10, 17),
                    "25029ccd-4034-467e-8aac-d4149d0f83b2"
                ),
                FindFlight(
                    "?", "localhost", "LAX", "SMF",
                    LocalDate.of(2021, 10, 11),
                    LocalDate.of(2021, 10, 17),
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

}