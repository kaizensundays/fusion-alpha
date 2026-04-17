package com.kaizensundays.flights.service

import com.kaizensundays.flights.service.dao.FindFlightDao
import com.kaizensundays.flights.service.dao.FindFlightLoader
import com.kaizensundays.flights.service.dao.FlightRepository
import com.kaizensundays.flights.service.messages.AddAirline
import com.kaizensundays.flights.service.messages.Event
import com.kaizensundays.flights.service.messages.FindFlight
import com.kaizensundays.flights.service.messages.FlightExt
import com.kaizensundays.flights.service.messages.Journal
import com.kaizensundays.ignite.quorum.NodeState
import com.zaxxer.hikari.HikariDataSource
import org.apache.ignite.Ignite
import org.apache.ignite.IgniteCache
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.persistence.autoconfigure.EntityScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.ImportResource
import org.springframework.context.annotation.Primary
import org.springframework.core.Ordered
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.BlockingQueue
import javax.sql.DataSource

/**
 * Created: Saturday 9/25/2021, 1:44 PM Eastern Time
 *
 * @author Sergey Chuykov
 */
@Configuration
@EnableAutoConfiguration
@ImportResource("classpath:service-config.xml")
@EnableConfigurationProperties(NodeProperties::class)
@EntityScan(basePackageClasses = [FlightExt::class])
@EnableJpaRepositories(basePackageClasses = [FlightRepository::class])
@Import(IgniteContext::class, JournalContext::class)
open class ServiceContext {

    @Value("\${pg.url}")
    var pgUrl = ""

    @Value("\${pg.user}")
    var pgUser = ""

    @Value("\${pg.password}")
    var pgPassword = ""

    @Bean
    @Primary
    @Qualifier("pgDataSource")
    open fun pgDataSource(): DataSource {
        val ds = HikariDataSource()
        ds.jdbcUrl = pgUrl
        ds.username = pgUser
        ds.password = pgPassword
        ds.driverClassName = "org.postgresql.Driver"
        return ds
    }

    @Bean
    open fun jdbc(@Qualifier("pgDataSource") pgDataSource: DataSource) = NamedParameterJdbcTemplate(pgDataSource)

    @Bean
    open fun findFlightDao(jdbc: NamedParameterJdbcTemplate): FindFlightDao {
        return FindFlightDao(jdbc)
    }

    @Bean
    open fun requestsCache(ignite: Ignite): IgniteCache<String, FindFlight> {
        return ignite.getOrCreateCache(CacheName.Requests)
    }

    @Bean
    open fun resultsCache(ignite: Ignite): IgniteCache<String, String> {
        return ignite.getOrCreateCache(CacheName.Results)
    }

    @Bean
    open fun findFlightHandler(requestsCache: IgniteCache<String, FindFlight>): FindFlightHandler {
        return FindFlightHandler(requestsCache)
    }

    @Bean
    open fun frontEndWebSocketHandler(findFlightHandler: FindFlightHandler): FrontEndWebSocketHandler {
        return FrontEndWebSocketHandler(findFlightHandler)
    }

    @Bean
    open fun findFlightLoader(ignite: Ignite, @Qualifier("pgDataSource") pgDataSource: DataSource) = FindFlightLoader(ignite, pgDataSource)

    @Bean
    open fun handlerAdapter() = WebSocketHandlerAdapter()

    @Bean
    open fun handlerMapping(frontEndWebSocketHandler: FrontEndWebSocketHandler): SimpleUrlHandlerMapping {
        val map = mapOf(
            "/ws/frontend" to frontEndWebSocketHandler
        )

        val mapping = SimpleUrlHandlerMapping()
        mapping.urlMap = map
        mapping.order = Ordered.HIGHEST_PRECEDENCE

        return mapping
    }

    @Bean
    open fun handlers(findFlightHandler: FindFlightHandler): Map<Class<out Event>, Handler<Event>> {
        val map: Map<Class<out Event>, Handler<*>> = mapOf(
            AddAirline::class.java to AddAirlineHandler(),
            FindFlight::class.java to findFlightHandler
        )
        @Suppress("UNCHECKED_CAST")
        return map as Map<Class<out Event>, Handler<Event>>
    }

    @Bean
    open fun messageQueue(): BlockingQueue<Journal> {
        return ArrayBlockingQueue(1000)
    }

    @Bean
    open fun defaultEventRoute(messageQueue: BlockingQueue<Journal>, journalManager: JournalManager, handlers: Map<Class<out Event>, Handler<Event>>): DefaultEventRoute {
        journalManager.messageQueue = messageQueue
        return DefaultEventRoute(messageQueue, journalManager, handlers)
    }

    @Bean
    open fun nodeState(@Value("\${cluster.quorum}") quorum: Int, ignite: Ignite, frontEndWebSocketHandler: FrontEndWebSocketHandler): NodeState {
        val nodeState = NodeState(quorum, ignite)
        nodeState.addListener(frontEndWebSocketHandler)
        return nodeState
    }

    @Bean
    open fun defaultRestController(
        resultsCache: IgniteCache<String, String>,
        defaultEventRoute: DefaultEventRoute,
        journalManager: JournalManager
    ): DefaultRestController {
        return DefaultRestController(resultsCache, defaultEventRoute, journalManager)
    }

}