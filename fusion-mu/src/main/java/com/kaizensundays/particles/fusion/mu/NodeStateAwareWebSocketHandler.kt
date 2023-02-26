package com.kaizensundays.particles.fusion.mu

import org.reactivestreams.Subscription
import org.springframework.web.reactive.socket.CloseStatus
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketSession
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Created: Sunday 2/26/2023, 11:13 AM Eastern Time
 *
 * @author Sergey Chuykov
 */
abstract class NodeStateAwareWebSocketHandler : WebSocketHandler, NodeStateListener {

    private val active = AtomicBoolean(false)

    private val sessionMap = mutableMapOf<String, WebSocketSession>()

    protected fun onSubscribe(subscription: Subscription, session: WebSocketSession) {
        if (active.get()) {
            sessionMap[session.id] = session
        } else {
            subscription.cancel()
        }
    }

    override fun onStateChange(active: Boolean) {
        this.active.set(active)

        if (!active) {
            sessionMap.keys.forEach { sessionId ->
                val session = sessionMap[sessionId]
                session?.close(CloseStatus.GOING_AWAY)
                sessionMap.remove(sessionId)
            }
        }
    }

}