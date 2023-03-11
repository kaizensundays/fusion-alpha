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
            mapOf("tableName" to tableName.uppercase()), Boolean::class.java
        ) ?: false
    }

    fun createTable(): Int {
        return jdbc.update(
            "CREATE TABLE IF NOT EXISTS journal (" +
                    " ID BIGINT auto_increment PRIMARY KEY, STATE INT," +
                    " TIME VARCHAR(32)," +
                    " UUID VARCHAR(36)," +
                    " MSG VARCHAR(1000)" +
                    ")",
            mapOf<String, Any>()
        )
    }

    fun findAll(): List<Journal> {
        return jdbc.query("select * from journal", this)
    }

    fun truncate(): Int {
        return jdbc.update("truncate table journal", mapOf<String, Any>())
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