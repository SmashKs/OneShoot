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

import org.assertj.core.api.Assertions.assertThat
import smash.ks.com.ext.const.GeneratorFactory.randomString
import smash.ks.com.ext.const.GeneratorFactory.zero
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class TakeUnlessDefaultTest {
    private var num = zero
    private var added = zero

    @BeforeTest
    fun init() {
        num = GeneratorFactory.randomInt
        added = GeneratorFactory.randomInt
    }

    @Test
    fun `make int takeUnlessDefault true`() {
        val defaultInt = -1
        val ans1 = num + added
        val ans2 = num - added

        DEFAULT_INT.takeUnlessDefault { num += added }
        assertNotEquals(ans1, num)
        defaultInt.takeUnlessDefault { num -= added }
        assertNotEquals(ans2, num)
    }

    @Test
    fun `make int takeUnlessDefault true else`() {
        val defaultInt = -1

        assertThat(DEFAULT_INT.takeUnlessDefault { num += added } ?: added).isEqualTo(added)
        assertThat(defaultInt.takeUnlessDefault { num -= added } ?: added).isEqualTo(added)
    }

    @Test
    fun `make int takeUnlessDefault false`() {
        val zeroNum = zero
        val negNum = -GeneratorFactory.randomInt
        val posNum = GeneratorFactory.randomInt

        assertThat(zeroNum.takeUnlessDefault { num + it }).isEqualTo(num)
        assertThat(negNum.takeUnlessDefault { num + it }).isEqualTo(num + negNum)
        assertThat(posNum.takeUnlessDefault { num + it }).isEqualTo(num + posNum)
    }

    @Test
    fun `make long takeUnlessDefault true`() {
        val defaultLong = -1L
        val ans1 = num + added
        val ans2 = num - added

        DEFAULT_LONG.takeUnlessDefault { num += added }
        assertNotEquals(ans1, num)
        defaultLong.takeUnlessDefault { num -= added }
        assertNotEquals(ans2, num)
    }

    @Test
    fun `make long takeUnlessDefault true else`() {
        val defaultLong = -1L

        assertThat(DEFAULT_LONG.takeUnlessDefault { num += added } ?: added).isEqualTo(added)
        assertThat(defaultLong.takeUnlessDefault { num -= added } ?: added).isEqualTo(added)
    }

    @Test
    fun `make long takeUnlessDefault false`() {
        val zeroNum = zero.toLong()
        val negNum = -GeneratorFactory.randomLong
        val posNum = GeneratorFactory.randomLong

        assertThat(zeroNum.takeUnlessDefault { num + it }).isEqualTo(num.toLong())
        assertThat(negNum.takeUnlessDefault { num + it }).isEqualTo((num + negNum))
        assertThat(posNum.takeUnlessDefault { num + it }).isEqualTo(num + posNum)
    }

    @Test
    fun `make string takeUnlessDefault true`() {
        val defaultStr = ""
        val ans1 = num + added
        val ans2 = num - added

        DEFAULT_STR.takeUnlessDefault { num += added }
        assertNotEquals(ans1, num)
        defaultStr.takeUnlessDefault { num -= added }
        assertNotEquals(ans2, num)
    }

    @Test
    fun `make string takeUnlessDefault true else`() {
        val defaultStr = ""

        assertThat(DEFAULT_STR.takeUnlessDefault { num += added } ?: added).isEqualTo(added)
        assertThat(defaultStr.takeUnlessDefault { num -= added } ?: added).isEqualTo(added)
    }

    @Test
    fun `make string takeUnlessDefault false`() {
        val str1 = randomString
        val str2 = "        "

        val ans1 = num + added
        val ans2 = num

        str1.takeUnlessDefault { num += added }
        assertEquals(ans1, num)
        str2.takeUnlessDefault { num -= added }
        assertEquals(ans2, num)
    }

    @Test
    fun `make double takeUnlessDefault true`() {
        val defaultDouble = -1.0
        val ans1 = num + added
        val ans2 = num - added

        DEFAULT_DOUBLE.takeUnlessDefault { num += added }
        assertNotEquals(ans1, num)
        defaultDouble.takeUnlessDefault { num -= added }
        assertNotEquals(ans2, num)
    }

    @Test
    fun `make double takeUnlessDefault true else`() {
        val defaultDouble = -1.0

        assertThat(DEFAULT_DOUBLE.takeUnlessDefault { num += added } ?: added).isEqualTo(added)
        assertThat(defaultDouble.takeUnlessDefault { num -= added } ?: added).isEqualTo(added)
    }

    @Test
    fun `make double takeUnlessDefault false`() {
        val zeroNum = zero.toDouble()
        val negNum = -GeneratorFactory.randomDouble
        val posNum = GeneratorFactory.randomDouble

        assertThat(zeroNum.takeUnlessDefault { num + it }).isEqualTo(num.toDouble())
        assertThat(negNum.takeUnlessDefault { num + it }).isEqualTo((num + negNum))
        assertThat(posNum.takeUnlessDefault { num + it }).isEqualTo(num + posNum)
    }

    @Test
    fun `make float takeUnlessDefault true`() {
        val defaultFloat = -1f
        val ans1 = num + added
        val ans2 = num - added

        DEFAULT_FLOAT.takeUnlessDefault { num += added }
        assertNotEquals(ans1, num)
        defaultFloat.takeUnlessDefault { num -= added }
        assertNotEquals(ans2, num)
    }

    @Test
    fun `make float takeUnlessDefault true else`() {
        val defaultFloat = -1f

        assertThat(DEFAULT_FLOAT.takeUnlessDefault { num += added } ?: added).isEqualTo(added)
        assertThat(defaultFloat.takeUnlessDefault { num -= added } ?: added).isEqualTo(added)
    }

    @Test
    fun `make float takeUnlessDefault false`() {
        val zeroNum = zero.toFloat()
        val negNum = -GeneratorFactory.randomFloat
        val posNum = GeneratorFactory.randomFloat

        assertThat(zeroNum.takeUnlessDefault { num + it }).isEqualTo(num.toFloat())
        assertThat(negNum.takeUnlessDefault { num + it }).isEqualTo((num + negNum))
        assertThat(posNum.takeUnlessDefault { num + it }).isEqualTo(num + posNum)
    }
}
