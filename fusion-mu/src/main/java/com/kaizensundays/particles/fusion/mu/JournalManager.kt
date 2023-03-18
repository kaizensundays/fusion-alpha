package com.kaizensundays.particles.fusion.mu

import com.kaizensundays.particles.fusion.mu.dao.JournalDao
import com.kaizensundays.particles.fusion.mu.messages.Event
import com.kaizensundays.particles.fusion.mu.messages.JacksonObjectConverter
import com.kaizensundays.particles.fusion.mu.messages.Journal
import com.kaizensundays.particles.fusion.mu.messages.JournalState
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.BlockingQueue

/**
 * Created: Sunday 3/12/2023, 1:33 PM Eastern Time
 *
 * @author Sergey Chuykov
 */
class JournalManager(private val journalDao: JournalDao) {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    private val jsonConverter = JacksonObjectConverter<Event>()

    lateinit var messageQueue: BlockingQueue<Journal>

    fun load() {

        val journals = journalDao.findByState(JournalState.ACCEPTED.value)

        if (journals.isNotEmpty()) {
            logger.info("Recovering ${journals.size} accepted events")
        }

        journals.forEach { journal ->
            journal.event = jsonConverter.toObject(journal.msg)
            messageQueue.put(journal)
            logger.info(journal.toString())
        }

        logger.info("Loaded ${journals.size} accepted events")
    }

    fun commit(journal: Journal): Int {
        return journalDao.updateStateByUUID(journal.uuid, JournalState.COMMITTED)
    }

}