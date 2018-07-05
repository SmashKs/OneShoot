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

package smash.ks.com.oneshoot.entities

import com.nhaarman.mockito_kotlin.verify
import org.junit.Assert.assertEquals
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import smash.ks.com.domain.Label
import smash.ks.com.ext.const.DEFAULT_FLOAT
import smash.ks.com.ext.const.DEFAULT_INT
import smash.ks.com.oneshoot.GeneratorFactory.randomInt
import smash.ks.com.oneshoot.GeneratorFactory.randomString
import kotlin.test.BeforeTest
import kotlin.test.Test

class LabelEntityTest {
    private var id = DEFAULT_INT
    private lateinit var label: Label
    private var confidence = DEFAULT_FLOAT
    @Mock private lateinit var entity: LabelEntity

    @BeforeTest
    fun setup() {
        MockitoAnnotations.initMocks(this)

        id = randomInt
        label = randomString

        entity.also {
            it.entryId = id
            it.label = label
            it.confidence = confidence
        }
    }

    @Test
    fun `assign all variable to new object and get them`() {
        entity = LabelEntity(id, label, confidence)

        assertEquals(id, entity.entryId)
        assertEquals(label, entity.label)
        assertEquals(confidence, entity.confidence)
    }

    @Test
    fun `create a new object then assign new variables`() {
        verify(entity).entryId = id
        verify(entity).label = label
        verify(entity).confidence = confidence
    }
}
