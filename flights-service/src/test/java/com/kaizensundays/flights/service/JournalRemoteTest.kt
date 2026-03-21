package com.kaizensundays.flights.service

import com.kaizensundays.flights.service.dao.JournalDao
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals

/**
 * Created: Saturday 3/4/2023, 11:53 AM Eastern Time
 *
 * @author Sergey Chuykov & Junie (gpt-5-2025-08-07)
 */
@ActiveProfiles("test")
@ExtendWith(SpringExtension::class)
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