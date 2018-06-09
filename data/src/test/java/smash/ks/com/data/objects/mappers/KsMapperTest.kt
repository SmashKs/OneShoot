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

package smash.ks.com.data.objects.mappers

import org.modelmapper.ModelMapper
import smash.ks.com.data.GeneratorFactory.randomLong
import smash.ks.com.data.GeneratorFactory.randomString
import smash.ks.com.data.models.KsModel
import smash.ks.com.data.models.mappers.KsMapper
import smash.ks.com.domain.datas.KsData
import smash.ks.com.ext.const.DEFAULT_LONG
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class KsMapperTest {
    private var ksId = DEFAULT_LONG
    private lateinit var ksUri: String
    private lateinit var mapper: ModelMapper
    private lateinit var ksMapper: KsMapper

    @BeforeTest
    fun setup() {
        ksId = randomLong
        ksUri = randomString
        mapper = ModelMapper()
        ksMapper = KsMapper(mapper)
    }

    @Test
    fun `mapping model to object`() {
        val model = KsModel(ksId, ksUri)
        val newObj = ksMapper.toDataFrom(model)

        assertEqualsObject(newObj, model)
    }

    @Test
    fun `mapping object to model`() {
        val obj = KsData(ksId, ksUri)
        val newModel = ksMapper.toModelFrom(obj)

        assertEqualsObject(obj, newModel)
    }

    private fun assertEqualsObject(obj: KsData, newModel: KsModel) {
        assertEquals(obj.uri, newModel.uri)
    }
}
