package com.kaizensundays.particles.fusion.mu

/**
 * Created: Sunday 2/26/2023, 11:13 AM Eastern Time
 *
 * @author Sergey Chuykov
 */
fun interface NodeStateListener {

    fun onStateChange(active: Boolean)

}