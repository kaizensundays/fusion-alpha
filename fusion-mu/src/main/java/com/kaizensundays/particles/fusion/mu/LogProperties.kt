package com.kaizensundays.particles.fusion.mu

import org.springframework.boot.context.properties.ConfigurationProperties
import java.io.File

/**
 * Created: Saturday 3/4/2023, 8:10 PM Eastern Time
 *
 * @author Sergey Chuykov
 */
@ConfigurationProperties(prefix = "log")
class LogProperties {

    var h2Path: String = ""
    var h2DatabaseName: String = ""
    var h2User: String = ""
    var h2Password: String = ""

    fun h2Url(): String {
        val path = File(h2Path)
        if (!path.isDirectory) {
            throw IllegalArgumentException("'$h2Path' not found")
        }
        return "jdbc:h2:" + path.canonicalPath + File.separator + h2DatabaseName
    }

}