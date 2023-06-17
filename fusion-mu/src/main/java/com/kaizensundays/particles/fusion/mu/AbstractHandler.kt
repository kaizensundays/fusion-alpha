package com.kaizensundays.particles.fusion.mu

import com.kaizensundays.particles.fusion.mu.messages.Event
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Created: Sunday 3/12/2023, 1:07 PM Eastern Time
 *
 * @author Sergey Chuykov
 */
abstract class AbstractHandler<E : Event> : Handler<E> {

    protected val logger: Logger = LoggerFactory.getLogger(javaClass)

    abstract fun doHandle(event: E)

    override fun handle(event: E) {
        try {
            doHandle(event)
        } catch (e: Exception) {
            logger.error(e.message, e)
        }
    }

}