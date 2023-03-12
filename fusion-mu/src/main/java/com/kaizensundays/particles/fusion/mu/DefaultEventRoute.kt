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
import java.util.concurrent.ArrayBlockingQueue
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
    private val handlers: Map<Class<out Event>, Handler<Event>>
) {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    private val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS z")

    private val jsonConverter = JacksonObjectConverter<Event>()

    private val defaultExecutor: ExecutorService = Executors.newSingleThreadExecutor { r -> Thread(r, "D") }

    private val queue = ArrayBlockingQueue<Event>(1000)

    private val journalExecutor: ExecutorService = Executors.newSingleThreadExecutor { r -> Thread(r, "J") }

    private val running = AtomicBoolean(true)

    init {
        df.timeZone = TimeZone.getTimeZone("America/New_York")
    }

    private fun execute(event: Event) {

        val handler = handlers[event.javaClass]

        @Suppress("IfThenToSafeAccess")
        if (handler != null) {
            handler.handle(event)
        }
    }

    private fun execute() {

        while (running.get()) {
            val event = queue.poll(1, TimeUnit.SECONDS)
            if (event != null) {
                execute(event)
            }
        }
    }

    private fun journal(event: Event) {

        val msg = jsonConverter.fromObject(event)
        logger.info(msg)

        val journal = Journal(0, JournalState.ACCEPTED.value, df.format(Date()), UUID.randomUUID().toString(), msg)

        journalDao.insert(journal)

        queue.put(event)
    }

    fun handle(event: Event) {
        logger.info("" + event)

        journalExecutor.execute { journal(event) }
    }

    private fun load() {

        val journals = journalDao.findByState(JournalState.ACCEPTED.value)

        journals.forEach { journal ->
            val event = jsonConverter.toObject(journal.msg)
            queue.put(event)
        }
    }

    @PostConstruct
    fun start() {

        defaultExecutor.execute { load() }

        defaultExecutor.execute { execute() }

        logger.info("Started")
    }

}