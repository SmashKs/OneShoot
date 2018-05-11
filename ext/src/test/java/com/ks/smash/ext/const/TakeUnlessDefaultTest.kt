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

import org.assertj.core.api.Assertions.assertThat
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class TakeUnlessDefaultTest {
    private var num = 0
    private var added = 0

    @BeforeTest
    fun init() {
        num = 32
        added = 4
    }

    @Test
    fun makeIntTakeUnlessDefaultTrue() {
        val defaultInt = -1
        val ans1 = num + added
        val ans2 = num - added

        DEFAULT_INT.takeUnlessDefault { num += added }
        assertNotEquals(ans1, num)
        defaultInt.takeUnlessDefault { num -= added }
        assertNotEquals(ans2, num)
    }

    @Test
    fun makeIntTakeUnlessDefaultTrueElse() {
        val defaultInt = -1

        assertThat(DEFAULT_INT.takeUnlessDefault { num += added } ?: added).isEqualTo(added)
        assertThat(defaultInt.takeUnlessDefault { num -= added } ?: added).isEqualTo(added)
    }

    @Test
    fun makeIntTakeUnlessDefaultFalse() {
        val zeroNum = 0
        val negNum = -5
        val posNum = 2

        assertThat(zeroNum.takeUnlessDefault { num + it }).isEqualTo(num)
        assertThat(negNum.takeUnlessDefault { num + it }).isEqualTo(num + negNum)
        assertThat(posNum.takeUnlessDefault { num + it }).isEqualTo(num + posNum)
    }

    @Test
    fun makeLongTakeUnlessDefaultTrue() {
        val defaultLong = -11L
        val ans1 = num + added
        val ans2 = num - added

        DEFAULT_LONG.takeUnlessDefault { num += added }
        assertNotEquals(ans1, num)
        defaultLong.takeUnlessDefault { num -= added }
        assertNotEquals(ans2, num)
    }

    @Test
    fun makeLongTakeUnlessDefaultTrueElse() {
        val defaultLong = -1L

        assertThat(DEFAULT_LONG.takeUnlessDefault { num += added } ?: added).isEqualTo(added)
        assertThat(defaultLong.takeUnlessDefault { num -= added } ?: added).isEqualTo(added)
    }

    @Test
    fun makeLongTakeUnlessDefaultFalse() {
        val zeroNum = 0L
        val negNum = -5L
        val posNum = 2L

        assertThat(zeroNum.takeUnlessDefault { num + it }).isEqualTo(num.toLong())
        assertThat(negNum.takeUnlessDefault { num + it }).isEqualTo((num + negNum))
        assertThat(posNum.takeUnlessDefault { num + it }).isEqualTo(num + posNum)
    }

    @Test
    fun makeStringTakeUnlessDefaultTrue() {
        val defaultStr = ""
        val ans1 = num + added
        val ans2 = num - added

        DEFAULT_STR.takeUnlessDefault { num += added }
        assertNotEquals(ans1, num)
        defaultStr.takeUnlessDefault { num -= added }
        assertNotEquals(ans2, num)
    }

    @Test
    fun makeStringTakeUnlessDefaultTrueElse() {
        val defaultStr = ""

        assertThat(DEFAULT_STR.takeUnlessDefault { num += added } ?: added).isEqualTo(added)
        assertThat(defaultStr.takeUnlessDefault { num -= added } ?: added).isEqualTo(added)
    }

    @Test
    fun makeStringTakeUnlessDefaultFalse() {
        val str1 = "smash ks"
        val str2 = "        "

        val ans1 = num + added
        val ans2 = num

        str1.takeUnlessDefault { num += added }
        assertEquals(ans1, num)
        str2.takeUnlessDefault { num -= added }
        assertEquals(ans2, num)
    }

    @Test
    fun makeDoubleTakeUnlessDefaultTrue() {
        val defaultDouble = -1.0
        val ans1 = num + added
        val ans2 = num - added

        DEFAULT_DOUBLE.takeUnlessDefault { num += added }
        assertNotEquals(ans1, num)
        defaultDouble.takeUnlessDefault { num -= added }
        assertNotEquals(ans2, num)
    }

    @Test
    fun makeDoubleTakeUnlessDefaultFalseElse() {
        val defaultDouble = -1.0

        assertThat(DEFAULT_DOUBLE.takeUnlessDefault { num += added } ?: added).isEqualTo(added)
        assertThat(defaultDouble.takeUnlessDefault { num -= added } ?: added).isEqualTo(added)
    }

    @Test
    fun makeDoubleTakeUnlessDefaultFalse() {
        val zeroNum = 0.0
        val negNum = -5.1
        val posNum = 2.32

        assertThat(zeroNum.takeUnlessDefault { num + it }).isEqualTo(num.toDouble())
        assertThat(negNum.takeUnlessDefault { num + it }).isEqualTo((num + negNum))
        assertThat(posNum.takeUnlessDefault { num + it }).isEqualTo(num + posNum)
    }

    @Test
    fun makeFloatTakeUnlessDefaultTrue() {
        val defaultFloat = -1f
        val ans1 = num + added
        val ans2 = num - added

        DEFAULT_FLOAT.takeUnlessDefault { num += added }
        assertNotEquals(ans1, num)
        defaultFloat.takeUnlessDefault { num -= added }
        assertNotEquals(ans2, num)
    }

    @Test
    fun makeFloatTakeUnlessDefaultFalseElse() {
        val defaultFloat = -1f

        assertThat(DEFAULT_FLOAT.takeUnlessDefault { num += added } ?: added).isEqualTo(added)
        assertThat(defaultFloat.takeUnlessDefault { num -= added } ?: added).isEqualTo(added)
    }

    @Test
    fun makeFloatTakeUnlessDefaultFalse() {
        val zeroNum = 0.00f
        val negNum = -5.12f
        val posNum = 2.4f

        assertThat(zeroNum.takeUnlessDefault { num + it }).isEqualTo(num.toFloat())
        assertThat(negNum.takeUnlessDefault { num + it }).isEqualTo((num + negNum))
        assertThat(posNum.takeUnlessDefault { num + it }).isEqualTo(num + posNum)
    }
}