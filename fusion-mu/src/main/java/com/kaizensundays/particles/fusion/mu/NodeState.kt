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
class NodeState(private val ignite: Ignite) : IgnitePredicate<Event>, NodeStateListener {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    companion object {
        const val CLUSTER_VOTES_PROP = "cluster.votes"
        const val CLUSTER_QUORUM_PROP = "cluster.quorum"
    }

    private val active = AtomicBoolean(false)

    val nodeStateListeners = mutableListOf<NodeStateListener>()

    override fun onStateChange(active: Boolean) {

        this.active.set(active)

        logger.info("Node is active: $active")

        nodeStateListeners.forEach { listener -> listener.onStateChange(active) }
    }

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

        require(set.size == 1) { "Configuration error" }

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

            val active = isActive(nodes)

            onStateChange(active)
        }

        return true
    }

    @PostConstruct
    fun start() {

        val nodes = ignite.cluster().forServers().nodes()

        print(nodes)

        val active = isActive(nodes)

        onStateChange(active)

        logger.info("Stared({${active}})")
    }

}