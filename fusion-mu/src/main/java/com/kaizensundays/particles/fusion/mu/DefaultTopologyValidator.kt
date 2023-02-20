package com.kaizensundays.particles.fusion.mu

import org.apache.ignite.cluster.ClusterNode
import org.apache.ignite.configuration.TopologyValidator
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Created: Monday 2/20/2023, 11:16 AM Eastern Time
 *
 * @author Sergey Chuykov
 */
class DefaultTopologyValidator : TopologyValidator {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    override fun validate(nodes: MutableCollection<ClusterNode>): Boolean {

        logger.info("nodes={}", nodes)

        nodes.forEach { node ->
            val votes = node.attribute<String>("fusion-mu.votes")?.toInt() ?:0
            val id = node.id().toString()
            logger.info("{}:{}", id, votes)
        }

        return true
    }

}