package com.kaizensundays.particles.fusion.mu

import com.kaizensundays.particles.fusion.mu.messages.Event
import com.kaizensundays.particles.fusion.mu.messages.FindFlight
import com.kaizensundays.particles.fusion.mu.messages.Flight
import com.kaizensundays.particles.fusion.mu.messages.JacksonObjectConverter
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

/**
 * Created: Monday 9/27/2021, 1:27 PM Eastern Time
 *
 * @author Sergey Chuykov
 */
class FrontEndWebSocketHandler(
    private val handler: FindFlightHandler,
    private val webSocketSessionMap: MutableMap<String, WebSocketSession>
) : WebSocketHandler {

    private val converter = JacksonObjectConverter<Event>()

    fun handleEvent(event: Event): Flux<Flight> {

        return when (event) {
            is FindFlight ->
                handler.getFlights(Flux.just(event))
            else ->
                Flux.empty()
        }
    }

    val active = true

    override fun handle(session: WebSocketSession): Mono<Void> {

        val subscriber = session.receive()
            .doOnSubscribe { subscription ->
                if (active) {
                    webSocketSessionMap[session.id] = session
                } else {
                    subscription.cancel()
                }
            }
            .map { msg -> msg.payloadAsText }
            .log()
            .publishOn(Schedulers.boundedElastic())
            .map { json -> converter.toObject(json) }
            .flatMap { event -> handleEvent(event) }
            .map { flight -> converter.fromObject(flight) }
            .log()
            .map { json -> session.textMessage(json) }


        return session.send(subscriber)
    }

}