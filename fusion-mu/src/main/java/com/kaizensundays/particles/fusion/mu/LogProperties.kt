package com.kaizensundays.particles.fusion.mu

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * Created: Saturday 3/4/2023, 8:10 PM Eastern Time
 *
 * @author Sergey Chuykov
 */
@ConfigurationProperties(prefix = "log")
class LogProperties {

    var h2Url: String = ""
    var h2User: String = ""
    var h2Password: String = ""

}