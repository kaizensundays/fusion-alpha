package com.kaizensundays.particles.fusion.mu.dao

import com.kaizensundays.particles.fusion.mu.messages.Journal
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet

/**
 * Created: Saturday 3/4/2023, 12:09 PM Eastern Time
 *
 * @author Sergey Chuykov
 */
class JournalRowMapper : RowMapper<Journal> {
    override fun mapRow(rs: ResultSet, rowNum: Int): Journal {
        return Journal(
            rs.getLong("id"),
            rs.getInt("state"),
            rs.getString("time"),
            rs.getString("uuid"),
            rs.getString("msg")
        )
    }
}