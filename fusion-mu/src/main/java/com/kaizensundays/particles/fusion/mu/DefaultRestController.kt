package com.kaizensundays.particles.fusion.mu

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping

/**
 * Created: Saturday 12/4/2021, 12:29 PM Eastern Time
 *
 * @author Sergey Chuykov
 */
@RestController
class DefaultRestController(
    private val handlerMapping: SimpleUrlHandlerMapping
) {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    @ResponseBody
    @RequestMapping("/ping")
    fun ping(): String {
        return "Ok"
    }

    @ResponseBody
    @RequestMapping("/removeMapping")
    fun removeMapping(): String {

        val handler = handlerMapping.urlMap.remove("/ws/frontend")

        logger.info("Disabled: {}", handler)

        return "Ok"
    }

}