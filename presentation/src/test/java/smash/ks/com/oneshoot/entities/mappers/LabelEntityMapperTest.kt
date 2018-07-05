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

package smash.ks.com.oneshoot.entities.mappers

import org.modelmapper.ModelMapper
import smash.ks.com.domain.Label
import smash.ks.com.domain.models.LabelModel
import smash.ks.com.ext.const.DEFAULT_FLOAT
import smash.ks.com.ext.const.DEFAULT_INT
import smash.ks.com.oneshoot.GeneratorFactory.randomFloat
import smash.ks.com.oneshoot.GeneratorFactory.randomInt
import smash.ks.com.oneshoot.GeneratorFactory.randomString
import smash.ks.com.oneshoot.entities.LabelEntity
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class LabelEntityMapperTest {
    private var id = DEFAULT_INT
    private var confidence = DEFAULT_FLOAT
    private lateinit var label: Label
    private lateinit var labelMapper: LabelEntityMapper

    @BeforeTest
    fun setup() {
        id = randomInt
        confidence = randomFloat
        label = randomString
        labelMapper = LabelEntityMapper(ModelMapper())
    }

    @Test
    fun `mapping entity to model`() {
        val entity = LabelEntity(id, label, confidence)
        val newModel = labelMapper.toModelFrom(entity)

        assertEqualsObject(newModel, entity)
    }

    @Test
    fun `mapping model to entity`() {
        val model = LabelModel(id, label, confidence)
        val newEntity = labelMapper.toEntityFrom(model)

        assertEqualsObject(model, newEntity)
    }

    private fun assertEqualsObject(model: LabelModel, newModel: LabelEntity) {
        assertEquals(model.entryId, newModel.entryId)
        assertEquals(model.label, newModel.label)
        assertEquals(model.confidence, newModel.confidence)
    }
}
