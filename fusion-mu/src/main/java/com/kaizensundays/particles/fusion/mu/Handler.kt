package com.kaizensundays.particles.fusion.mu

import com.kaizensundays.particles.fusion.mu.messages.Event

/**
 * Created: Saturday 10/16/2021, 1:46 PM Eastern Time
 *
 * @author Sergey Chuykov
 */
fun interface Handler<E : Event> {

    fun handle(event: E)

}