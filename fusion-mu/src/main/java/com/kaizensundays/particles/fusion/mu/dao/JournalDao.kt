package com.kaizensundays.particles.fusion.mu.dao

import com.kaizensundays.particles.fusion.mu.messages.Journal
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

/**
 * Created: Saturday 3/4/2023, 11:56 AM Eastern Time
 *
 * @author Sergey Chuykov
 */
class JournalDao(private val jdbc: NamedParameterJdbcTemplate) {

    private val rowMapper = LogRowMapper()

    fun findAll(): List<Journal> {
        return jdbc.query("select * from journal", rowMapper)
    }

}