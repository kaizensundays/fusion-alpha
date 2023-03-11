package com.kaizensundays.particles.fusion.mu.dao

import com.kaizensundays.particles.fusion.mu.JournalContext
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner

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

    @Test
    fun findAll() {
        val exist = dao.tableExist("journal")
    }

}