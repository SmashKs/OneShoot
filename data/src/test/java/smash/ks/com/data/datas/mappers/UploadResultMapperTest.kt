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
import smash.ks.com.data.GeneratorFactory.randomInt
import smash.ks.com.data.GeneratorFactory.randomString
import smash.ks.com.data.datas.DataUploadResultMapper
import smash.ks.com.data.datas.Tag
import smash.ks.com.data.datas.UploadResultData
import smash.ks.com.domain.models.UploadResultModel
import smash.ks.com.ext.const.DEFAULT_INT
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class UploadResultMapperTest {
    private lateinit var format: String
    private lateinit var resourceType: String
    private lateinit var secureUrl: String
    private lateinit var createdAt: String
    private lateinit var publicId: String
    private var width = DEFAULT_INT
    private var height = DEFAULT_INT
    private var placeholder = false
    private lateinit var tags: List<Tag>
    private lateinit var uploadResultMapper: DataUploadResultMapper

    @BeforeTest
    fun setup() {
        format = randomString
        resourceType = randomString
        secureUrl = randomString
        createdAt = randomString
        publicId = randomString
        width = randomInt
        height = randomInt
        tags = listOf()
        uploadResultMapper = UploadResultMapper(ModelMapper())
    }

    @Test
    fun `mapping data to model`() {
        val data =
            UploadResultData(format, resourceType, secureUrl, createdAt, publicId, width, height, placeholder, tags)
        val newModel = uploadResultMapper.toModelFrom(data)

        assertEqualsObject(data, newModel)
    }

    @Test
    fun `mapping model to data`() {
        val model =
            UploadResultModel(format, resourceType, secureUrl, createdAt, publicId, width, height, placeholder, tags)
        val newData = uploadResultMapper.toDataFrom(model)

        assertEqualsObject(newData, model)
    }

    private fun assertEqualsObject(data: UploadResultData, newModel: UploadResultModel) {
        assertEquals(data.format, newModel.format)
        assertEquals(data.resourceType, newModel.resourceType)
        assertEquals(data.secureUrl, newModel.secureUrl)
        assertEquals(data.createdAt, newModel.createdAt)
        assertEquals(data.publicId, newModel.publicId)
        assertEquals(data.width, newModel.width)
        assertEquals(data.height, newModel.height)
        assertEquals(data.placeholder, newModel.placeholder)
        assertEquals(data.tags, newModel.tags)
    }
}
