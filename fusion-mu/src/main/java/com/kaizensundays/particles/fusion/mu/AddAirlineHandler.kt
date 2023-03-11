package com.kaizensundays.particles.fusion.mu

import com.kaizensundays.particles.fusion.mu.messages.AddAirline
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Created: Saturday 3/11/2023, 12:11 PM Eastern Time
 *
 * @author Sergey Chuykov
 */
class AddAirlineHandler : Handler<AddAirline> {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    override fun handle(event: AddAirline) {
        logger.info(event.toString())
    }

}