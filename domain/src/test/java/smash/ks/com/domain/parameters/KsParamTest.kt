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

package smash.ks.com.domain.parameters

import org.assertj.core.api.Assertions.assertThat
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class KsParamTest {
    companion object {
        private const val IMAGE_ID = 52
    }

    private lateinit var ksParam: KsParam

    @BeforeTest
    fun setup() {
        ksParam = KsParam(IMAGE_ID)
    }

    @Test
    fun hashCount() {
        val fieldsSize = KsParam::class.java.declaredFields.size

        assertThat(ksParam.toParameter().values.size).isEqualTo(fieldsSize)
    }

    @Test
    fun checkToHashMap() {
        assertThat(ksParam.toParameter()).isInstanceOf(HashMap::class.java)
    }

    @Test
    fun checkTransitionNotNull() {
        assertThat(ksParam.toParameter()).isNotNull
    }

    @Test
    fun checkMapContent() {
        val map = ksParam.toParameter()

        assertNotNull(map["id"])
        assertEquals(IMAGE_ID.toString(), map["id"])
    }
}