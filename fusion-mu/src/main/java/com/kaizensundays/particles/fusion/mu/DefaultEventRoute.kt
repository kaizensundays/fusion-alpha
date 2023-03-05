package com.kaizensundays.particles.fusion.mu

import com.kaizensundays.particles.fusion.mu.messages.Event
import org.slf4j.Logger
import org.slf4j.LoggerFactory


/**
 * Created: Sunday 3/5/2023, 11:24 AM Eastern Time
 *
 * @author Sergey Chuykov
 */
class DefaultEventRoute {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    fun handle(event: Event) {
        logger.info("" + event)
    }

}