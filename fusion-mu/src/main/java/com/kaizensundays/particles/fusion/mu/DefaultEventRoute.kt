package com.kaizensundays.particles.fusion.mu

import com.kaizensundays.particles.fusion.mu.dao.JournalDao
import com.kaizensundays.particles.fusion.mu.messages.Event
import com.kaizensundays.particles.fusion.mu.messages.JacksonObjectConverter
import com.kaizensundays.particles.fusion.mu.messages.Journal
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


/**
 * Created: Sunday 3/5/2023, 11:24 AM Eastern Time
 *
 * @author Sergey Chuykov
 */
class DefaultEventRoute(
    private val journalDao: JournalDao
) {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    private val jsonConverter = JacksonObjectConverter<Event>()

    private val journalExecutor: ExecutorService = Executors.newSingleThreadExecutor { r -> Thread(r, "J") }

    private fun journal(event: Event) {

        val msg = jsonConverter.fromObject(event)
        logger.info(msg)

        val journal = Journal(0, 0, msg)

        journalDao.insert(journal)
    }

    fun handle(event: Event) {
        logger.info("" + event)

        journalExecutor.execute { journal(event) }
    }

}