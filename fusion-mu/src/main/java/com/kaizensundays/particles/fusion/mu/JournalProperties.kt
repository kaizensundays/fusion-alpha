package com.kaizensundays.particles.fusion.mu

import org.springframework.boot.context.properties.ConfigurationProperties
import java.io.File

/**
 * Created: Saturday 3/4/2023, 8:10 PM Eastern Time
 *
 * @author Sergey Chuykov
 */
@ConfigurationProperties(prefix = "journal")
class JournalProperties {

    var h2Path: String = ""
    var h2DatabaseName: String = ""
    var h2User: String = ""
    var h2Password: String = ""

    fun h2Url(): String {
        val path = File(h2Path)
        if (!path.exists()) {
            if (!path.mkdirs()) {
                throw IllegalStateException("Unable to create directory" + path.canonicalPath)
            }
        }
        return "jdbc:h2:" + path.canonicalPath + File.separator + h2DatabaseName
    }

}