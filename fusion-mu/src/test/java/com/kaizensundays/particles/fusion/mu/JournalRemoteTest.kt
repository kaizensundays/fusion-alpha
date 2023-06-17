package com.kaizensundays.particles.fusion.mu

import com.kaizensundays.particles.fusion.mu.dao.JournalDao
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import kotlin.test.assertEquals

/**
 * Created: Saturday 3/4/2023, 11:53 AM Eastern Time
 *
 * @author Sergey Chuykov
 */
@ActiveProfiles("test")
@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = [JournalContext::class])
class JournalRemoteTest : MuTestSupport() {

    @Autowired
    lateinit var dao: JournalDao

    @Test
    fun findAll() {

        val journal = dao.findAll()

        assertEquals(3, journal.size)
    }

}