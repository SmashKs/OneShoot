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

package smash.ks.com.data.datas.mappers

import org.modelmapper.ModelMapper
import smash.ks.com.data.GeneratorFactory.randomFloat
import smash.ks.com.data.GeneratorFactory.randomInt
import smash.ks.com.data.GeneratorFactory.randomString
import smash.ks.com.data.datas.DataLabelMapper
import smash.ks.com.data.datas.LabelData
import smash.ks.com.domain.Label
import smash.ks.com.domain.models.LabelModel
import smash.ks.com.ext.const.DEFAULT_FLOAT
import smash.ks.com.ext.const.DEFAULT_INT
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class LabelMapperTest {
    private var id = DEFAULT_INT
    private var confidence = DEFAULT_FLOAT
    private lateinit var label: Label
    private lateinit var labelMapper: DataLabelMapper

    @BeforeTest
    fun setup() {
        id = randomInt
        confidence = randomFloat
        label = randomString
        labelMapper = LabelMapper(ModelMapper())
    }

    @Test
    fun `mapping data to model`() {
        val data = LabelData(id, label, confidence)
        val newModel = labelMapper.toModelFrom(data)

        assertEqualsObject(data, newModel)
    }

    @Test
    fun `mapping model to data`() {
        val model = LabelModel(id, label, confidence)
        val newData = labelMapper.toDataFrom(model)

        assertEqualsObject(newData, model)
    }

    private fun assertEqualsObject(data: LabelData, newModel: LabelModel) {
        assertEquals(data.entryId, newModel.entryId)
        assertEquals(data.label, newModel.label)
        assertEquals(data.confidence, newModel.confidence)
    }
}
