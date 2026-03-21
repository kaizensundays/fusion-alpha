package com.kaizensundays.flights.service

import com.kaizensundays.flights.service.messages.FindFlight
import org.apache.ignite.Ignition
import org.apache.ignite.configuration.ClientConfiguration
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import java.time.Duration
import java.util.*


/**
 * Created: Sunday 4/20/2025, 12:18 PM Eastern Time
 *
 * @author Sergey Chuykov & Junie (gpt-5-2025-08-07)
 */
class CacheClientRemoteTest : MuTestSupport() {

    var requests = emptyMap<String, Array<FindFlight>>()

    @BeforeEach
    fun before() {
        val json = Flights.read("/find-flights.json")

        requests = converter.toObjects(json, Flights.findFlightTypeRef)

        requests.mergeValues().forEach { request -> request.uuid = UUID.randomUUID().toString() }
    }

    fun requests(): Flux<FindFlight> {
        return Flux.fromIterable(requests.mergeValues())
            .delaySubscription(Duration.ofSeconds(10))
            .delayElements(Duration.ofSeconds(10))
    }

    @Test
    fun test() {

        val cfg = ClientConfiguration()
            .setAddresses("127.0.0.1:10801")

        try {
            Ignition.startClient(cfg).use { client ->

                val cache = client.cache<String, FindFlight>(CacheName.Requests)

                requests.mergeValues()
                    .forEach { req -> cache.put(req.uuid, req) }

            }
        } catch (e: Exception) {
            logger.error("", e)
        }
    }

}