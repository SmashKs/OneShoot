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
import smash.ks.com.data.objects.KsModel
import smash.ks.com.domain.objects.KsObject
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class KsMapperTest {
    companion object {
        private const val KS_ID = 147L
        private const val KS_URI = "this is test uri!"
    }

    private lateinit var mapper: ModelMapper
    private lateinit var ksMapper: KsMapper

    @BeforeTest
    fun setup() {
        mapper = ModelMapper()
        ksMapper = KsMapper(mapper)
    }

    @Test
    fun `mapping model to object`() {
        val model = KsModel(KS_ID, KS_URI)
        val newObj = ksMapper.toObjectFrom(model)

        assertEqualsObject(newObj, model)
    }

    @Test
    fun `mapping object to model`() {
        val obj = KsObject(KS_URI)
        val newModel = ksMapper.toModelFrom(obj)

        assertEqualsObject(obj, newModel)
    }

    private fun assertEqualsObject(obj: KsObject, newModel: KsModel) {
        assertEquals(obj.uri, newModel.uri)
    }
}