package com.kaizensundays.particles.fusion.mu

import com.kaizensundays.particles.fusion.mu.dao.JournalDao
import com.kaizensundays.particles.fusion.mu.messages.Event
import com.kaizensundays.particles.fusion.mu.messages.JacksonObjectConverter
import com.kaizensundays.particles.fusion.mu.messages.Journal
import com.kaizensundays.particles.fusion.mu.messages.JournalState
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


/**
 * Created: Sunday 3/5/2023, 11:24 AM Eastern Time
 *
 * @author Sergey Chuykov
 */
class DefaultEventRoute(
    private val journalDao: JournalDao,
    private val handlers: Map<Class<out Event>, Handler<Event>>
) {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    private val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSz")

    private val jsonConverter = JacksonObjectConverter<Event>()

    private val defaultExecutor: ExecutorService = Executors.newSingleThreadExecutor { r -> Thread(r, "D") }
    private val journalExecutor: ExecutorService = Executors.newSingleThreadExecutor { r -> Thread(r, "J") }

    init {
        df.timeZone = TimeZone.getTimeZone("UTC")
    }

    private fun execute(event: Event) {

        val handler = handlers[event.javaClass]

        @Suppress("IfThenToSafeAccess")
        if (handler != null) {
            handler.handle(event)
        }
    }

    private fun journal(event: Event) {

        val msg = jsonConverter.fromObject(event)
        logger.info(msg)

        val journal = Journal(0, JournalState.ACCEPTED.value, df.format(Date()), UUID.randomUUID().toString(), msg)

        journalDao.insert(journal)

        defaultExecutor.execute { execute(event) }
    }

    fun handle(event: Event) {
        logger.info("" + event)

        journalExecutor.execute { journal(event) }
    }

}