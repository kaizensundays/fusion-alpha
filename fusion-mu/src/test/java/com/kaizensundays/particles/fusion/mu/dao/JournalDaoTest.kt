package com.kaizensundays.particles.fusion.mu.dao

import com.kaizensundays.particles.fusion.mu.JournalContext
import com.kaizensundays.particles.fusion.mu.messages.Journal
import com.kaizensundays.particles.fusion.mu.messages.JournalState
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import java.util.*
import kotlin.test.assertEquals

/**
 * Created: Saturday 3/11/2023, 11:13 AM Eastern Time
 *
 * @author Sergey Chuykov
 */
@ActiveProfiles("test")
@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = [JournalContext::class])
class JournalDaoTest {

    @Autowired
    lateinit var dao: JournalDao

    private fun journal(msg: String): Journal {
        return Journal(
            0, JournalState.ACCEPTED.value, Date(),
            UUID.randomUUID().toString(), msg
        )
    }

    private val journals = listOf(
        journal("{1}"),
        journal("{2}"),
        journal("{3}"),
    )

    @Before
    fun before() {
        val exist = dao.tableExist("journal")
        if (exist) {
            dao.truncate()
        } else {
            dao.createTable()
        }
    }

    @Test
    fun insert() {

        journals.forEach { journal -> dao.insert(journal) }

        val list = dao.findAll()

        assertEquals(journals.size, list.size)
    }

/*
    @Test
    fun findAll() {

        dao.findAll()

    }
*/

}