/*
 * Copyright (C) 2018 The Smash Ks Open Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ks.smash.ext.const

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DefaultTest {
    @Test
    fun `make default int true`() {
        val defaultNum = -1

        assertTrue { defaultNum.isDefault() }
        assertTrue { DEFAULT_INT.isDefault() }
    }

    @Test
    fun `make default int false`() {
        val zeroNum = 0
        val negNum = -4
        val posNum = 3

        assertFalse { zeroNum.isDefault() }
        assertFalse { negNum.isDefault() }
        assertFalse { posNum.isDefault() }
    }

    @Test
    fun `make default long true`() {
        val defaultNum = -1

        assertTrue { defaultNum.isDefault() }
        assertTrue { DEFAULT_LONG.isDefault() }
    }

    @Test
    fun `make default long false`() {
        val zeroNum = 0L
        val negNum = -4L
        val posNum = 3L

        assertFalse { zeroNum.isDefault() }
        assertFalse { negNum.isDefault() }
        assertFalse { posNum.isDefault() }
    }

    @Test
    fun `make default string true`() {
        val defaultNum = ""

        assertTrue { defaultNum.isDefault() }
        assertTrue { DEFAULT_STR.isDefault() }
    }

    @Test
    fun `make default string false`() {
        val str = "smash ks"

        assertFalse { str.isDefault() }
    }

    @Test
    fun `make default double true`() {
        val defaultNum = -1.0

        assertTrue { defaultNum.isDefault() }
        assertTrue { DEFAULT_DOUBLE.isDefault() }
    }

    @Test
    fun `make default double false`() {
        val zeroNum = 0.41
        val negNum = -4.12
        val posNum = 3.85

        assertFalse { zeroNum.isDefault() }
        assertFalse { negNum.isDefault() }
        assertFalse { posNum.isDefault() }
    }

    @Test
    fun `make default float true`() {
        val defaultNum = -1f

        assertTrue { defaultNum.isDefault() }
        assertTrue { DEFAULT_FLOAT.isDefault() }
    }

    @Test
    fun `make default float false`() {
        val zeroNum = 0.3f
        val negNum = -4.11f
        val posNum = 3.98f

        assertFalse { zeroNum.isDefault() }
        assertFalse { negNum.isDefault() }
        assertFalse { posNum.isDefault() }
    }
}