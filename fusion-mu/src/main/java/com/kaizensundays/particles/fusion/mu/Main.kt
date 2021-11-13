package com.kaizensundays.particles.fusion.mu

import org.springframework.boot.builder.SpringApplicationBuilder

/**
 * Created: Sunday 6/20/2021, 11:11 AM Eastern Time
 *
 * @author Sergey Chuykov
 */
object Main {

    @JvmStatic
    fun main(args: Array<String>) {

        SpringApplicationBuilder(ServiceContext::class.java)
                .build().run(*args)

    }

}