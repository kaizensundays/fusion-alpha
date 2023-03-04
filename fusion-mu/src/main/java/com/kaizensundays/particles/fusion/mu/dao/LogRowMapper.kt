package com.kaizensundays.particles.fusion.mu.dao

import com.kaizensundays.particles.fusion.mu.messages.Log
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet

/**
 * Created: Saturday 3/4/2023, 12:09 PM Eastern Time
 *
 * @author Sergey Chuykov
 */
class LogRowMapper : RowMapper<Log> {
    override fun mapRow(rs: ResultSet, rowNum: Int): Log {
        return Log(
            rs.getLong("id"),
            rs.getString("msg"),
            rs.getInt("state"),
        )
    }
}