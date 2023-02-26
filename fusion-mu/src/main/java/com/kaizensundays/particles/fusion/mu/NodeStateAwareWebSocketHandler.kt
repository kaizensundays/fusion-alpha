package com.kaizensundays.particles.fusion.mu

import org.reactivestreams.Subscription
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.reactive.socket.CloseStatus
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketSession
import java.time.Duration
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Created: Sunday 2/26/2023, 11:13 AM Eastern Time
 *
 * @author Sergey Chuykov
 */
abstract class NodeStateAwareWebSocketHandler : WebSocketHandler, NodeStateListener {

    protected val logger: Logger = LoggerFactory.getLogger(javaClass)

    private val active = AtomicBoolean(false)

    private val sessionMap = mutableMapOf<String, WebSocketSession>()

    protected fun onSubscribe(subscription: Subscription, session: WebSocketSession) {
        if (active.get()) {
            sessionMap[session.id] = session
        } else {
            subscription.cancel()
        }
    }

    fun closeSession(session: WebSocketSession?) {
        if (session != null) {
            logger.info("Closing session: ${session.id}")
            try {
                session.close(CloseStatus.GOING_AWAY)
                    .doOnSuccess {
                        val closed = !session.isOpen
                        logger.info("Session closed=${closed}")
                    }
                    .block(Duration.ofSeconds(10))
                sessionMap.remove(session.id)
            } catch (e: Exception) {
                logger.error(e.message, e)
            }
        }
    }

    override fun onStateChange(active: Boolean) {
        this.active.set(active)

        if (!active) {
            HashSet(sessionMap.keys).forEach { sessionId ->
                val session = sessionMap[sessionId]
                closeSession(session)
            }
        }
    }

}