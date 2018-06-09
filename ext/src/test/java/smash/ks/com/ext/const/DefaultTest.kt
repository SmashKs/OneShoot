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

package smash.ks.com.ext.const

import smash.ks.com.ext.const.GeneratorFactory.randomDouble
import smash.ks.com.ext.const.GeneratorFactory.randomFloat
import smash.ks.com.ext.const.GeneratorFactory.randomInt
import smash.ks.com.ext.const.GeneratorFactory.randomLong
import smash.ks.com.ext.const.GeneratorFactory.randomString
import smash.ks.com.ext.const.GeneratorFactory.zero
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DefaultTest {
    @Test
    fun `make default int true`() {
        assertTrue { DEFAULT_INT.isDefault() }
    }

    @Test
    fun `make default int false`() {
        val zeroNum = zero
        val negNum = -randomInt
        val posNum = randomInt

        assertFalse { zeroNum.isDefault() }
        assertFalse { negNum.isDefault() }
        assertFalse { posNum.isDefault() }
    }

    @Test
    fun `make default long true`() {
        assertTrue { DEFAULT_LONG.isDefault() }
    }

    @Test
    fun `make default long false`() {
        val zeroNum = zero.toLong()
        val negNum = -randomLong
        val posNum = randomLong

        assertFalse { zeroNum.isDefault() }
        assertFalse { negNum.isDefault() }
        assertFalse { posNum.isDefault() }
    }

    @Test
    fun `make default string true`() {
        assertTrue { DEFAULT_STR.isDefault() }
    }

    @Test
    fun `make default string false`() {
        val str = randomString

        assertFalse { str.isDefault() }
    }

    @Test
    fun `make default double true`() {
        assertTrue { DEFAULT_DOUBLE.isDefault() }
    }

    @Test
    fun `make default double false`() {
        val zeroNum = zero.toDouble()
        val negNum = -randomDouble
        val posNum = randomDouble

        assertFalse { zeroNum.isDefault() }
        assertFalse { negNum.isDefault() }
        assertFalse { posNum.isDefault() }
    }

    @Test
    fun `make default float true`() {
        assertTrue { DEFAULT_FLOAT.isDefault() }
    }

    @Test
    fun `make default float false`() {
        val zeroNum = zero.toDouble()
        val negNum = -randomFloat
        val posNum = randomFloat

        assertFalse { zeroNum.isDefault() }
        assertFalse { negNum.isDefault() }
        assertFalse { posNum.isDefault() }
    }
}
