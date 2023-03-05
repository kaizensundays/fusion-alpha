package com.kaizensundays.particles.fusion.mu.messages

/**
 * Created: Saturday 3/4/2023, 11:55 AM Eastern Time
 *
 * @author Sergey Chuykov
 */
data class Journal(
    val id: Long,
    val msg: String,
    val state: Int
) : Event