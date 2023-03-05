package com.kaizensundays.particles.fusion.mu

import com.kaizensundays.particles.fusion.mu.dao.LogDao
import org.h2.jdbcx.JdbcDataSource
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import javax.sql.DataSource

/**
 * Created: Saturday 3/4/2023, 8:03 PM Eastern Time
 *
 * @author Sergey Chuykov
 */
@Configuration
@EnableConfigurationProperties(value = [LogProperties::class])
open class LogContext {

    @Bean
    open fun h2LogDataSource(props: LogProperties): DataSource {
        val ds = JdbcDataSource()
        ds.setURL(props.h2Url())
        ds.user = props.h2User
        ds.password = props.h2Password
        return ds
    }

    @Bean
    open fun h2Jdbc(h2LogDataSource: DataSource) = NamedParameterJdbcTemplate(h2LogDataSource)

    @Bean
    open fun h2LogDao(h2Jdbc: NamedParameterJdbcTemplate) = LogDao(h2Jdbc)

}