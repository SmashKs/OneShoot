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

package smash.ks.com.data.datas

import com.nhaarman.mockito_kotlin.verify
import org.junit.Assert.assertEquals
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import smash.ks.com.data.GeneratorFactory.randomInt
import smash.ks.com.data.GeneratorFactory.randomString
import smash.ks.com.domain.Label
import smash.ks.com.ext.const.DEFAULT_FLOAT
import smash.ks.com.ext.const.DEFAULT_INT
import kotlin.test.BeforeTest
import kotlin.test.Test

class LabelDataTest {
    private var id = DEFAULT_INT
    private lateinit var label: Label
    private var confidence = DEFAULT_FLOAT
    @Mock private lateinit var data: LabelData

    @BeforeTest
    fun setup() {
        MockitoAnnotations.initMocks(this)

        id = randomInt
        label = randomString

        data.also {
            it.entryId = id
            it.label = label
            it.confidence = confidence
        }
    }

    @Test
    fun `assign all variable to new data and get them`() {
        data = LabelData(id, label, confidence)

        assertEquals(id, data.entryId)
        assertEquals(label, data.label)
        assertEquals(confidence, data.confidence)
    }

    @Test
    fun `create a new data then assign new variables`() {
        verify(data).entryId = id
        verify(data).label = label
        verify(data).confidence = confidence
    }
}
