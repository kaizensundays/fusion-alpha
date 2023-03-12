package com.kaizensundays.particles.fusion.mu

import com.kaizensundays.particles.fusion.mu.messages.AddAirline
import java.lang.Thread.sleep

/**
 * Created: Saturday 3/11/2023, 12:11 PM Eastern Time
 *
 * @author Sergey Chuykov
 */
class AddAirlineHandler : AbstractHandler<AddAirline>() {

    override fun doHandle(event: AddAirline) {
        logger.info(event.toString())

        logger.info("Step 1")
        sleep(1000)

        logger.info("Step 2")
        sleep(1000)

        logger.info("Step 3")
        sleep(1000)
    }

}