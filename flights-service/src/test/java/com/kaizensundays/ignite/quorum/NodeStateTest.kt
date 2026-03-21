package com.kaizensundays.ignite.quorum

import org.apache.ignite.Ignite
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Created: Saturday 2/25/2023, 11:16 AM Eastern Time
 *
 * @author Sergey Chuykov
 */
class NodeStateTest {

    private val ignite: Ignite = mock()

    private val state = NodeState(2, ignite)

    @Test
    fun isActive() {

        assertFalse(state.isActive(listOf(mock())))
        assertTrue(state.isActive(listOf(mock(), mock())))
        assertTrue(state.isActive(listOf(mock(), mock(), mock())))
    }

}