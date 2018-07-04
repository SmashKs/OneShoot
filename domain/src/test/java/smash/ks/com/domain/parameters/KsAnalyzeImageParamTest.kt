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
import smash.ks.com.domain.parameters.KsAnalyzeImageParam.Companion.PARAM_BYTE_ARRAY
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class KsAnalyzeImageParamTest {
    private lateinit var byteArray: ByteArray
    private lateinit var ksAnalyzeImageParam: KsAnalyzeImageParam

    @BeforeTest
    fun setup() {
        byteArray = byteArrayOf()
        ksAnalyzeImageParam = KsAnalyzeImageParam(byteArray)
    }

    @Test
    fun `assign the data to fields`() {
        assertEquals(byteArray, ksAnalyzeImageParam.imageByteArray)
    }

    @Test
    fun `count of the hashmap after transition to the parameter`() {
        val fieldsSize = ksAnalyzeImageParam::class.java.declaredFields
            .filter { "private final" in it.toGenericString() }.size

        assertThat(ksAnalyzeImageParam.toAnyParameter().values.size).isEqualTo(fieldsSize)
    }

    @Test
    fun `assign and check the content is the same`() {
        val map = ksAnalyzeImageParam.toAnyParameter()

        assertNotNull(map[PARAM_BYTE_ARRAY])
    }

    @Test
    fun `check the instance hashmap after transition to the parameter`() {
        assertThat(ksAnalyzeImageParam.toAnyParameter()).isInstanceOf(HashMap::class.java)
    }

    @Test
    fun `check is not null after transition to the parameter`() {
        assertThat(ksAnalyzeImageParam.toAnyParameter()).isNotNull
    }

    @Test(UnsupportedOperationException::class)
    fun `toParameter is unsupported method`() {
        ksAnalyzeImageParam.toParameter()
    }
}
