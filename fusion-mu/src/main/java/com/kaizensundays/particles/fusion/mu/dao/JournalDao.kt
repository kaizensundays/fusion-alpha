package com.kaizensundays.particles.fusion.mu.dao

import com.kaizensundays.particles.fusion.mu.messages.Journal
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.sql.ResultSet

/**
 * Created: Saturday 3/4/2023, 11:56 AM Eastern Time
 *
 * @author Sergey Chuykov
 */
class JournalDao(private val jdbc: NamedParameterJdbcTemplate) : RowMapper<Journal> {

    override fun mapRow(rs: ResultSet, rowNum: Int): Journal {
        return Journal(
            rs.getLong("id"),
            rs.getInt("state"),
            rs.getString("time"),
            rs.getString("uuid"),
            rs.getString("msg")
        )
    }

    fun tableExist(tableName: String): Boolean {
        return jdbc.queryForObject(
            "SELECT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = :tableName)",
            mapOf("tableName" to tableName), Boolean::class.java
        ) ?: false
    }

    fun findAll(): List<Journal> {
        return jdbc.query("select * from journal", this)
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