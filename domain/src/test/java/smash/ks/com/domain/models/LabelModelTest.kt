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

package smash.ks.com.domain.models

import com.nhaarman.mockito_kotlin.verify
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import smash.ks.com.domain.GeneratorFactory.randomInt
import smash.ks.com.domain.GeneratorFactory.randomString
import smash.ks.com.domain.Label
import smash.ks.com.ext.const.DEFAULT_FLOAT
import smash.ks.com.ext.const.DEFAULT_INT
import kotlin.test.BeforeTest

class LabelModelTest {
    private var id = DEFAULT_INT
    private lateinit var label: Label
    private var confidence = DEFAULT_FLOAT
    @Mock private lateinit var model: LabelModel

    @BeforeTest
    fun setup() {
        MockitoAnnotations.initMocks(this)

        id = randomInt
        label = randomString

        model.also {
            it.entryId = id
            it.label = label
            it.confidence = confidence
        }
    }

    @Test
    fun `assign all variable to new model and get them`() {
        model = LabelModel(id, label, confidence)

        assertEquals(id, model.entryId)
        assertEquals(label, model.label)
        assertEquals(confidence, model.confidence)
    }

    @Test
    fun `create a new model then assign new variables`() {
        verify(model).entryId = id
        verify(model).label = label
        verify(model).confidence = confidence
    }
}
