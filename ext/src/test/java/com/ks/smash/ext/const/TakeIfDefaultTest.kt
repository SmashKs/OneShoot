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

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class TakeIfDefaultTest {
    private var num = 0
    private var added = 0

    private val fail1 = 44
    private val fail2 = 12
    private val fail3 = 99

    @BeforeTest
    fun init() {
        num = 32
        added = 4
    }

    @Test
    fun makeIntTakeIfDefaultTrue() {
        val defaultInt = -1
        val ans1 = num + added
        val ans2 = num

        DEFAULT_INT.takeIfDefault { num += added }
        assertEquals(ans1, num)
        defaultInt.takeIfDefault { num -= added }
        assertEquals(ans2, num)
    }

    @Test
    fun makeIntTakeIfDefaultFalse() {
        val zeroNum = 0
        val negNum = -5
        val posNum = 2

        val ans = num + added

        zeroNum.takeIfDefault { num += added }
        assertNotEquals(ans, num)
        negNum.takeIfDefault { num += added }
        assertNotEquals(ans, num)
        posNum.takeIfDefault { num += added }
        assertNotEquals(ans, num)
    }

    @Test
    fun makeIntTakeIfDefaultFalseElse() {
        val zeroNum = 0
        val negNum = -5
        val posNum = 2

        zeroNum.takeIfDefault { num += added } ?: run { num = fail1 }
        assertEquals(fail1, num)
        negNum.takeIfDefault { num -= added } ?: run { num = fail2 }
        assertEquals(fail2, num)
        posNum.takeIfDefault { num += added } ?: run { num = fail3 }
        assertEquals(fail3, num)
    }

    @Test
    fun makeLongTakeIfDefaultTrue() {
        val defaultLong = -1L
        val ans1 = num + added
        val ans2 = num

        DEFAULT_LONG.takeIfDefault { num += added }
        assertEquals(ans1, num)
        defaultLong.takeIfDefault { num -= added }
        assertEquals(ans2, num)
    }

    @Test
    fun makeLongTakeIfDefaultFalse() {
        val zeroNum = 0L
        val negNum = -5L
        val posNum = 2L

        val ans = num + added

        zeroNum.takeIfDefault { num += added }
        assertNotEquals(ans, num)
        negNum.takeIfDefault { num += added }
        assertNotEquals(ans, num)
        posNum.takeIfDefault { num += added }
        assertNotEquals(ans, num)
    }

    @Test
    fun makeLongTakeIfDefaultFalseElse() {
        val zeroNum = 0L
        val negNum = -5L
        val posNum = 2L

        zeroNum.takeIfDefault { num += added } ?: run { num = fail1 }
        assertEquals(fail1, num)
        negNum.takeIfDefault { num -= added } ?: run { num = fail2 }
        assertEquals(fail2, num)
        posNum.takeIfDefault { num += added } ?: run { num = fail3 }
        assertEquals(fail3, num)
    }

    @Test
    fun makeStringTakeIfDefaultTrue() {
        val defaultStr = ""
        val ans1 = num + added
        val ans2 = num

        DEFAULT_STR.takeIfDefault { num += added }
        assertEquals(ans1, num)
        defaultStr.takeIfDefault { num -= added }
        assertEquals(ans2, num)
    }

    @Test
    fun makeStringTakeIfDefaultFalse() {
        val str1 = "smash ks"
        val str2 = "        "

        val ans = num + added

        str1.takeIfDefault { num += added }
        assertNotEquals(ans, num)
        str2.takeIfDefault { num -= added }
        assertNotEquals(ans, num)
    }

    @Test
    fun makeStringTakeIfDefaultFalseElse() {
        val str1 = "smash ks"
        val str2 = "        "

        str1.takeIfDefault { num += added } ?: run { num = fail1 }
        assertEquals(fail1, num)
        str2.takeIfDefault { num -= added } ?: run { num = fail2 }
        assertEquals(fail2, num)
    }

    @Test
    fun makeDoubleTakeIfDefaultTrue() {
        val defaultDouble = -1L
        val ans1 = num + added
        val ans2 = num

        DEFAULT_DOUBLE.takeIfDefault { num += added }
        assertEquals(ans1, num)
        defaultDouble.takeIfDefault { num -= added }
        assertEquals(ans2, num)
    }

    @Test
    fun makeDoubleTakeIfDefaultFalse() {
        val zeroNum = 0.34
        val negNum = -5.1
        val posNum = 2.32

        val ans = num + added

        zeroNum.takeIfDefault { num += added }
        assertNotEquals(ans, num)
        negNum.takeIfDefault { num += added }
        assertNotEquals(ans, num)
        posNum.takeIfDefault { num += added }
        assertNotEquals(ans, num)
    }

    @Test
    fun makeDoubleTakeIfDefaultFalseElse() {
        val zeroNum = 0.34
        val negNum = -5.1
        val posNum = 2.32

        zeroNum.takeIfDefault { num += added } ?: run { num = fail1 }
        assertEquals(fail1, num)
        negNum.takeIfDefault { num -= added } ?: run { num = fail2 }
        assertEquals(fail2, num)
        posNum.takeIfDefault { num += added } ?: run { num = fail3 }
        assertEquals(fail3, num)
    }

    @Test
    fun makeFloatTakeIfDefaultTrue() {
        val defaultFloat = -1f
        val ans1 = num + added
        val ans2 = num

        DEFAULT_FLOAT.takeIfDefault { num += added }
        assertEquals(ans1, num)
        defaultFloat.takeIfDefault { num -= added }
        assertEquals(ans2, num)
    }

    @Test
    fun makeFloatTakeIfDefaultFalse() {
        val zeroNum = 0.00f
        val negNum = -5.12f
        val posNum = 2.4f

        val ans = num + added

        zeroNum.takeIfDefault { num += added }
        assertNotEquals(ans, num)
        negNum.takeIfDefault { num += added }
        assertNotEquals(ans, num)
        posNum.takeIfDefault { num += added }
        assertNotEquals(ans, num)
    }

    @Test
    fun makeFloatTakeIfDefaultFalseElse() {
        val zeroNum = 0.00f
        val negNum = -5.12f
        val posNum = 2.4f

        zeroNum.takeIfDefault { num += added } ?: run { num = fail1 }
        assertEquals(fail1, num)
        negNum.takeIfDefault { num -= added } ?: run { num = fail2 }
        assertEquals(fail2, num)
        posNum.takeIfDefault { num += added } ?: run { num = fail3 }
        assertEquals(fail3, num)
    }
}