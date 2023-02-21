package com.kaizensundays.particles.fusion.mu

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.socket.WebSocketSession

/**
 * Created: Saturday 12/4/2021, 12:29 PM Eastern Time
 *
 * @author Sergey Chuykov
 */
@RestController
class DefaultRestController(
    private val webSocketSessionMap: MutableMap<String, WebSocketSession>
) {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    @ResponseBody
    @RequestMapping("/ping")
    fun ping(): String {
        return "Ok"
    }

    @ResponseBody
    @RequestMapping("/sessions")
    fun sessions(): String {

        webSocketSessionMap.forEach { (id, session) ->
            logger.info("$id::${session.isOpen}::$session")
        }

        return "Ok"
    }

}