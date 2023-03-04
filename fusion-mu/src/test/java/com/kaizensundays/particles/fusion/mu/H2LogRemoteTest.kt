package com.kaizensundays.particles.fusion.mu

import com.kaizensundays.particles.fusion.mu.dao.LogDao
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import kotlin.test.assertEquals

/**
 * Created: Saturday 3/4/2023, 11:53 AM Eastern Time
 *
 * @author Sergey Chuykov
 */
@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = [ServiceContext::class])
class H2LogRemoteTest : MuTestSupport() {

    @Autowired
    lateinit var dao: LogDao

    @Test
    fun findAll() {

       val log = dao.findAll()

        assertEquals(1, log.size)
    }

}