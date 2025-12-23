package com.kaizensundays.ignite.quorum

import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.mockito.Mockito.mock

/**
 * Created: Saturday 4/19/2025, 12:34 PM Eastern Time
 *
 * @author Sergey Chuykov & Junie (gpt-5-2025-08-07)
 */
class DefaultTopologyValidatorTest {

    private val validator = DefaultTopologyValidator(2)

    @Test
    fun validate() {

        assertFalse(validator.validate(mutableListOf(mock())))
        assertTrue(validator.validate(mutableListOf(mock(), mock())))
        assertTrue(validator.validate(mutableListOf(mock(), mock(), mock())))
    }

}