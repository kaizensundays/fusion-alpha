package com.kaizensundays.particles.fusion.mu

import org.apache.ignite.Ignite
import org.apache.ignite.events.DiscoveryEvent
import org.apache.ignite.events.Event
import org.apache.ignite.lang.IgnitePredicate
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Created: Monday 2/20/2023, 12:10 PM Eastern Time
 *
 * @author Sergey Chuykov
 */
class BaselineChangedListener(private val ignite: Ignite) : IgnitePredicate<Event> {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    override fun apply(event: Event): Boolean {

        logger.info("event: {}", event)

        if (event is DiscoveryEvent) {
            val nodes = event.topologyNodes()
            nodes.forEach { node ->
                val votes = node.attribute<String>("fusion-mu.votes")?.toInt() ?: 0
                val id = node.id().toString()
                logger.info("{}:{}", id, votes)
            }
        }

        return true
    }

}