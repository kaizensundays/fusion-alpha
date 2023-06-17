package com.kaizensundays.particles.fusion.mu

import org.apache.ignite.events.CacheEvent
import org.apache.ignite.lang.IgnitePredicate

/**
 * Created: Monday 2/20/2023, 12:09 PM Eastern Time
 *
 * @author Sergey Chuykov
 */
interface IgniteEventListener : IgnitePredicate<CacheEvent> {
}