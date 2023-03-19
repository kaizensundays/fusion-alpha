package com.kaizensundays.particles.fusion.mu.messages

/**
 * Created: Sunday 3/19/2023, 2:07 PM Eastern Time
 *
 * @author Sergey Chuykov
 */
data class JournalFormatted(
    val id: Long,
    val state: Int,
    val time: String,
    val uuid: String,
    val msg: String,
    var event: Event,
): JacksonSerializable