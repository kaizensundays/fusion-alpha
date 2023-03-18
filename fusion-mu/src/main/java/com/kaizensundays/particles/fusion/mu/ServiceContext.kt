package com.kaizensundays.particles.fusion.mu

import com.kaizensundays.particles.fusion.mu.dao.FindFlightDao
import com.kaizensundays.particles.fusion.mu.dao.FindFlightLoader
import com.kaizensundays.particles.fusion.mu.dao.JournalDao
import com.kaizensundays.particles.fusion.mu.messages.AddAirline
import com.kaizensundays.particles.fusion.mu.messages.Event
import com.kaizensundays.particles.fusion.mu.messages.FindFlight
import com.kaizensundays.particles.fusion.mu.messages.Journal
import org.apache.ignite.Ignite
import org.apache.ignite.events.EventType
import org.postgresql.ds.PGPoolingDataSource
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.core.Ordered
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter
import java.util.*
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
@Import(IgniteContext::class, JournalContext::class)
open class ServiceContext {

    @Bean
    open fun dataSource(): DataSource {
        val ds = PGPoolingDataSource()
        ds.serverName = "PgSql"
        ds.portNumber = 30432
        ds.user = "postgres"
        ds.password = "postgres"
        return ds
    }


    @Bean
    open fun jdbc(dataSource: DataSource) = NamedParameterJdbcTemplate(dataSource)

    @Bean
    open fun findFlightDao(jdbc: NamedParameterJdbcTemplate): FindFlightDao {
        return FindFlightDao(jdbc)
    }

    @Bean
    open fun findFlightHandler(ignite: Ignite): FindFlightHandler {
        return FindFlightHandler(ignite.getOrCreateCache(CacheName.Requests))
    }

    @Bean
    open fun frontEndWebSocketHandler(findFlightHandler: FindFlightHandler): FrontEndWebSocketHandler {
        return FrontEndWebSocketHandler(findFlightHandler)
    }

    @Bean
    open fun findFlightLoader(ignite: Ignite, dataSource: DataSource) = FindFlightLoader(ignite, dataSource)

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
        return map as Map<Class<out Event>, Handler<Event>>
    }

    @Bean
    open fun messageQueue(): BlockingQueue<Journal> {
        return ArrayBlockingQueue(1000)
    }

    @Bean
    open fun defaultEventRoute(journalH2Dao: JournalDao, messageQueue: BlockingQueue<Journal>, journalManager: JournalManager, handlers: Map<Class<out Event>, Handler<Event>>): DefaultEventRoute {
        journalManager.messageQueue = messageQueue
        return DefaultEventRoute(journalH2Dao, messageQueue, journalManager, handlers)
    }

    @Bean
    open fun nodeState(ignite: Ignite, frontEndWebSocketHandler: FrontEndWebSocketHandler): NodeState {
        val nodeState = NodeState(ignite)
        nodeState.nodeStateListeners.add(frontEndWebSocketHandler)
        val events = ignite.events()
        events.localListen(nodeState, *EventType.EVTS_DISCOVERY)
        return nodeState
    }

    @Bean
    open fun defaultRestController(defaultEventRoute: DefaultEventRoute): DefaultRestController {
        return DefaultRestController(defaultEventRoute)
    }

}