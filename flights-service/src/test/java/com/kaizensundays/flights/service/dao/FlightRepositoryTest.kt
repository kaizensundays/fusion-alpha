package com.kaizensundays.flights.service.dao

import com.kaizensundays.flights.service.messages.FlightExt
import kotlin.test.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.data.jpa.autoconfigure.DataJpaRepositoriesAutoConfiguration
import org.springframework.boot.hibernate.autoconfigure.HibernateJpaAutoConfiguration
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration
import org.springframework.boot.jdbc.autoconfigure.DataSourceTransactionManagerAutoConfiguration
import org.springframework.boot.persistence.autoconfigure.EntityScan
import org.springframework.boot.transaction.autoconfigure.TransactionAutoConfiguration

/**
 * Created: Sunday 6/1/2025, 12:23 PM Eastern Time
 *
 * @author Sergey Chuykov & Junie (gpt-5-2025-08-07)
 */
@ImportAutoConfiguration(
    classes = [
        DataSourceAutoConfiguration::class,
        DataSourceTransactionManagerAutoConfiguration::class,
        DataJpaRepositoriesAutoConfiguration::class,
        HibernateJpaAutoConfiguration::class,
        TransactionAutoConfiguration::class
    ]
)
@EntityScan(basePackageClasses = [FlightExt::class])
@EnableJpaRepositories(basePackageClasses = [FlightRepository::class])
@ActiveProfiles("test")
@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = [JpaTestContext::class])
class FlightRepositoryTest {

    @Autowired
    lateinit var repo: FlightRepository

    @Test
    fun test() {

        val result = repo.findAll()

        assertNotNull(result)
    }

}