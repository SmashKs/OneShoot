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

import com.ks.smash.ext.const.generate.GeneratorFactory.randomDouble
import com.ks.smash.ext.const.generate.GeneratorFactory.randomFloat
import com.ks.smash.ext.const.generate.GeneratorFactory.randomInt
import com.ks.smash.ext.const.generate.GeneratorFactory.randomLong
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class TakeIfDefaultTest {
    companion object {
        private const val ZERO_NUM = 0
    }

    private var num = ZERO_NUM
    private var added = ZERO_NUM

    private val fail1 = randomInt
    private val fail2 = randomInt
    private val fail3 = randomInt

    @BeforeTest
    fun init() {
        num = randomInt
        added = randomInt
    }

    @Test
    fun `make int takeIfDefault true`() {
        val defaultInt = -1
        val ans1 = num + added
        val ans2 = num

        DEFAULT_INT.takeIfDefault { num += added }
        assertEquals(ans1, num)
        defaultInt.takeIfDefault { num -= added }
        assertEquals(ans2, num)
    }

    @Test
    fun `make Int takeIfDefault false`() {
        val zeroNum = ZERO_NUM
        val negNum = -randomInt
        val posNum = randomInt

        val ans = num + added

        zeroNum.takeIfDefault { num += added }
        assertNotEquals(ans, num)
        negNum.takeIfDefault { num += added }
        assertNotEquals(ans, num)
        posNum.takeIfDefault { num += added }
        assertNotEquals(ans, num)
    }

    @Test
    fun `make int takeIfDefault false else`() {
        val zeroNum = ZERO_NUM
        val negNum = -randomInt
        val posNum = randomInt

        zeroNum.takeIfDefault { num += added } ?: run { num = fail1 }
        assertEquals(fail1, num)
        negNum.takeIfDefault { num -= added } ?: run { num = fail2 }
        assertEquals(fail2, num)
        posNum.takeIfDefault { num += added } ?: run { num = fail3 }
        assertEquals(fail3, num)
    }

    @Test
    fun `make long takeIfDefault true`() {
        val defaultLong = -1L
        val ans1 = num + added
        val ans2 = num

        DEFAULT_LONG.takeIfDefault { num += added }
        assertEquals(ans1, num)
        defaultLong.takeIfDefault { num -= added }
        assertEquals(ans2, num)
    }

    @Test
    fun `make long takeIfDefault false`() {
        val zeroNum = ZERO_NUM.toLong()
        val negNum = -randomLong
        val posNum = randomLong

        val ans = num + added

        zeroNum.takeIfDefault { num += added }
        assertNotEquals(ans, num)
        negNum.takeIfDefault { num += added }
        assertNotEquals(ans, num)
        posNum.takeIfDefault { num += added }
        assertNotEquals(ans, num)
    }

    @Test
    fun `make long takeIfDefault false else`() {
        val zeroNum = ZERO_NUM.toLong()
        val negNum = -randomLong
        val posNum = randomLong

        zeroNum.takeIfDefault { num += added } ?: run { num = fail1 }
        assertEquals(fail1, num)
        negNum.takeIfDefault { num -= added } ?: run { num = fail2 }
        assertEquals(fail2, num)
        posNum.takeIfDefault { num += added } ?: run { num = fail3 }
        assertEquals(fail3, num)
    }

    @Test
    fun `make string takeIfDefault true`() {
        val defaultStr = ""
        val ans1 = num + added
        val ans2 = num

        DEFAULT_STR.takeIfDefault { num += added }
        assertEquals(ans1, num)
        defaultStr.takeIfDefault { num -= added }
        assertEquals(ans2, num)
    }

    @Test
    fun `make string takeIfDefault false`() {
        val str1 = "smash ks"
        val str2 = "        "

        val ans = num + added

        str1.takeIfDefault { num += added }
        assertNotEquals(ans, num)
        str2.takeIfDefault { num -= added }
        assertNotEquals(ans, num)
    }

    @Test
    fun `make string takeIfDefault false else`() {
        val str1 = "smash ks"
        val str2 = "        "

        str1.takeIfDefault { num += added } ?: run { num = fail1 }
        assertEquals(fail1, num)
        str2.takeIfDefault { num -= added } ?: run { num = fail2 }
        assertEquals(fail2, num)
    }

    @Test
    fun `make double takeIfDefault true`() {
        val defaultDouble = -1L
        val ans1 = num + added
        val ans2 = num

        DEFAULT_DOUBLE.takeIfDefault { num += added }
        assertEquals(ans1, num)
        defaultDouble.takeIfDefault { num -= added }
        assertEquals(ans2, num)
    }

    @Test
    fun `make double takeIfDefault false`() {
        val zeroNum = ZERO_NUM.toDouble()
        val negNum = -randomDouble
        val posNum = randomDouble

        val ans = num + added

        zeroNum.takeIfDefault { num += added }
        assertNotEquals(ans, num)
        negNum.takeIfDefault { num += added }
        assertNotEquals(ans, num)
        posNum.takeIfDefault { num += added }
        assertNotEquals(ans, num)
    }

    @Test
    fun `make double takeIfDefault false else`() {
        val zeroNum = ZERO_NUM.toDouble()
        val negNum = -randomDouble
        val posNum = randomDouble

        zeroNum.takeIfDefault { num += added } ?: run { num = fail1 }
        assertEquals(fail1, num)
        negNum.takeIfDefault { num -= added } ?: run { num = fail2 }
        assertEquals(fail2, num)
        posNum.takeIfDefault { num += added } ?: run { num = fail3 }
        assertEquals(fail3, num)
    }

    @Test
    fun `make float takeIfDefault true`() {
        val defaultFloat = -1f
        val ans1 = num + added
        val ans2 = num

        DEFAULT_FLOAT.takeIfDefault { num += added }
        assertEquals(ans1, num)
        defaultFloat.takeIfDefault { num -= added }
        assertEquals(ans2, num)
    }

    @Test
    fun `make float takeIfDefault false`() {
        val zeroNum = ZERO_NUM.toFloat()
        val negNum = -randomFloat
        val posNum = randomFloat

        val ans = num + added

        zeroNum.takeIfDefault { num += added }
        assertNotEquals(ans, num)
        negNum.takeIfDefault { num += added }
        assertNotEquals(ans, num)
        posNum.takeIfDefault { num += added }
        assertNotEquals(ans, num)
    }

    @Test
    fun `make float takeIfDefault false else`() {
        val zeroNum = ZERO_NUM.toFloat()
        val negNum = -randomFloat
        val posNum = randomFloat

        zeroNum.takeIfDefault { num += added } ?: run { num = fail1 }
        assertEquals(fail1, num)
        negNum.takeIfDefault { num -= added } ?: run { num = fail2 }
        assertEquals(fail2, num)
        posNum.takeIfDefault { num += added } ?: run { num = fail3 }
        assertEquals(fail3, num)
    }
}
