package com.kaizensundays.particles.fusion.mu

import com.kaizensundays.particles.fusion.mu.messages.AddAirline
import com.kaizensundays.particles.fusion.mu.messages.Journal
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

/**
 * Created: Saturday 12/4/2021, 12:29 PM Eastern Time
 *
 * @author Sergey Chuykov
 */
@RestController
class DefaultRestController(
    private val eventRoute: DefaultEventRoute,
    private val journalManager: JournalManager
) {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    @ResponseBody
    @RequestMapping("/ping")
    fun ping(): String {
        return "Ok"
    }

    @ResponseBody
    @PostMapping("/add/airline")
    fun addAirline(@RequestBody addAirline: AddAirline): String {

        eventRoute.handle(addAirline)

        return "Ok"
    }

    @GetMapping("/journal/findAll", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun journalFindAll(): List<Journal> {

        return journalManager.findAll()
    }

}