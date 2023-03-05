package com.kaizensundays.particles.fusion.mu.dao

import com.kaizensundays.particles.fusion.mu.messages.Journal
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

/**
 * Created: Saturday 3/4/2023, 11:56 AM Eastern Time
 *
 * @author Sergey Chuykov
 */
class JournalDao(private val jdbc: NamedParameterJdbcTemplate) {

    private val rowMapper = JournalRowMapper()

    fun findAll(): List<Journal> {
        return jdbc.query("select * from journal", rowMapper)
    }

    fun insert(journal: Journal): Int {
        return jdbc.update(
            "insert into JOURNAL (STATE,TIME, UUID, MSG) VALUES (:state, :time, :uuid, :msg)",
            mapOf(
                "state" to journal.state, "time" to journal.time, "uuid" to journal.uuid, "msg" to journal.msg,
                "x" to "X"
            )
        )
    }

}