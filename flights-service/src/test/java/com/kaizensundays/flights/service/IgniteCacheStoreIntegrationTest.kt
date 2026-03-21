package com.kaizensundays.flights.service

import com.kaizensundays.flights.service.dao.FindFlightDao
import com.kaizensundays.flights.service.dao.FindFlightLoader
import com.kaizensundays.flights.service.messages.FindFlight
import org.apache.ignite.Ignite
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.junit.jupiter.api.extension.ExtendWith
import reactor.core.publisher.Flux
import java.time.Duration
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/**
 * Created: Sunday 10/17/2021, 2:33 PM Eastern Time
 *
 * @author Sergey Chuykov
 */
@ActiveProfiles("test")
@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = [ServiceContext::class])
class IgniteCacheStoreIntegrationTest : MuTestSupport() {

    @Autowired
    lateinit var ignite: Ignite

    @Autowired
    lateinit var findFlightLoader: FindFlightLoader

    @Autowired
    lateinit var findFlightDao: FindFlightDao

    @Autowired
    lateinit var findFlightHandler: FindFlightHandler

    var requests = emptyMap<String, Array<FindFlight>>()

    @BeforeEach
    fun before() {

        val json = Flights.read("/find-flights.json")

        requests = converter.toObjects(json, Flights.findFlightTypeRef)

        requests.mergeValues().forEach { request -> request.uuid = UUID.randomUUID().toString() }

    }

    @Test
    fun loadCache() {

        val cache = ignite.getOrCreateCache<String, FindFlight>(CacheName.Requests)

        var count = cache.count()
        logger.info("count={}", count)

        findFlightLoader.loadFindFlight()

        count = cache.count()
        logger.info("count={}", count)

        val expected = findFlightDao.count()

        assertEquals(expected, count)
    }

    @Test
    fun getFlights() {

        val events = Flux.fromIterable(requests.mergeValues())
            .delaySubscription(Duration.ofMillis(300))
            .delayElements(Duration.ofMillis(300))

        val flights = findFlightHandler.getFlights(events)
            .collectList()
            .block(Duration.ofSeconds(30))

        assertNotNull(flights)
        assertEquals(4, flights.size)
    }

}