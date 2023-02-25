package com.kaizensundays.particles.fusion.mu

import org.apache.ignite.Ignite
import org.apache.ignite.cluster.ClusterNode
import org.apache.ignite.events.DiscoveryEvent
import org.apache.ignite.events.Event
import org.apache.ignite.lang.IgnitePredicate
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.atomic.AtomicBoolean
import javax.annotation.PostConstruct

/**
 * Created: Monday 2/20/2023, 12:10 PM Eastern Time
 *
 * @author Sergey Chuykov
 */
class NodeState(private val ignite: Ignite) : IgnitePredicate<Event> {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    companion object {
        const val CLUSTER_VOTES_PROP = "cluster.votes"
        const val CLUSTER_QUORUM_PROP = "cluster.quorum"
    }

    val active = AtomicBoolean(false)

    fun getIntAttribute(node: ClusterNode, name: String, default: Int): Int {
        return node.attribute<String>(name)?.toInt() ?: default
    }

    fun votes(node: ClusterNode): Int {
        return getIntAttribute(node, CLUSTER_VOTES_PROP, 0)
    }

    fun quorum(node: ClusterNode): Int {
        return getIntAttribute(node, CLUSTER_QUORUM_PROP, 0)
    }

    fun quorum(nodes: Collection<ClusterNode>): Int {

        val set = nodes.map { node -> quorum(node) }.toSet()

        if (set.size != 1) {
            throw IllegalStateException("Configuration error")
        }

        return set.first()
    }

    fun isActive(nodes: Collection<ClusterNode>): Boolean {

        val votes = nodes.sumOf { node -> votes(node) }

        val quorum = quorum(nodes)

        return votes > 0 && quorum > 0 && votes >= quorum
    }

    private fun print(nodes: Collection<ClusterNode>) {
        nodes.forEach { node ->
            val votes = votes(node)
            val id = node.id().toString()
            logger.info("{}:{}", id, votes)
        }
    }

    override fun apply(event: Event): Boolean {

        logger.info("event: {}", event)

        if (event is DiscoveryEvent) {
            val nodes = event.topologyNodes()

            print(nodes)

            active.set(isActive(nodes))

            logger.info("Node is active: ${active.get()}")
        }

        return true
    }

    @PostConstruct
    fun start() {

        val nodes = ignite.cluster().forServers().nodes()

        print(nodes)

        active.set(isActive(nodes))

        logger.info("Stared({${active.get()}})")
    }

}