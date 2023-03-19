package com.kaizensundays.particles.fusion.mu.messages

import com.fasterxml.jackson.annotation.JsonFormat
import java.util.*

/**
 * Created: Saturday 3/4/2023, 11:55 AM Eastern Time
 *
 * @author Sergey Chuykov
 */
data class Journal(
    val id: Long,
    val state: Int,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS zzzz", timezone = "America/New_York")
    val time: Date,
    val uuid: String,
    val msg: String,
    @Transient
    var event: Event? = null
) : JacksonSerializable