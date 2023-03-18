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
import java.util.concurrent.BlockingQueue
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import javax.annotation.PostConstruct

/**
 * Created: Sunday 3/5/2023, 11:24 AM Eastern Time
 *
 * @author Sergey Chuykov
 */
class DefaultEventRoute(
    private val journalDao: JournalDao,
    private val messageQueue: BlockingQueue<Journal>,
    private val journalManager: JournalManager,
    private val handlers: Map<Class<out Event>, Handler<Event>>
) {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    private val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS z")

    private val jsonConverter = JacksonObjectConverter<Event>()

    private val defaultExecutor: ExecutorService = Executors.newSingleThreadExecutor { r -> Thread(r, "D") }

    private val journalExecutor: ExecutorService = Executors.newSingleThreadExecutor { r -> Thread(r, "J") }

    private val running = AtomicBoolean(true)

    init {
        df.timeZone = TimeZone.getTimeZone("America/New_York")
    }

    private fun execute(journal: Journal) {

        val event = journal.event
        if (event != null) {

            val handler = handlers[event.javaClass]
            if (handler != null) {
                handler.handle(event)
            } else {
                logger.info("Unexpected message type: ${event.javaClass}")
            }
        }
        if (journalManager.commit(journal) == 0) {
            logger.error("Unable to commit journal")
        }
    }

    private fun execute() {

        while (running.get()) {
            val journal = messageQueue.poll(1, TimeUnit.SECONDS)
            if (journal != null) {
                execute(journal)
            }
        }
    }

    private fun journal(event: Event) {

        val msg = jsonConverter.fromObject(event)
        logger.info(msg)

        val journal = Journal(0, JournalState.ACCEPTED.value, df.format(Date()), UUID.randomUUID().toString(), msg, event)

        journalDao.insert(journal)

        messageQueue.put(journal)
    }

    fun handle(event: Event) {
        logger.info("" + event)

        journalExecutor.execute { journal(event) }
    }

    private fun load() {

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

    @PostConstruct
    fun start() {

        defaultExecutor.execute { load() }

        defaultExecutor.execute { execute() }

        logger.info("Started")
    }

}