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
import org.junit.Assert
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import smash.ks.com.data.datas.Tag
import smash.ks.com.oneshoot.GeneratorFactory
import java.util.Date
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class PhotoEntityTest {
    private lateinit var uri: String
    private lateinit var author: String
    private lateinit var title: String
    private lateinit var tags: List<Tag>
    private lateinit var uploadDate: Date
    @Mock private lateinit var entity: PhotoEntity

    @BeforeTest
    fun setup() {
        MockitoAnnotations.initMocks(this)

        uri = GeneratorFactory.randomString
        author = GeneratorFactory.randomString
        title = GeneratorFactory.randomString
        tags = GeneratorFactory.randomTags
        uploadDate = Date()

        entity.also {
            it.uri = uri
            it.author = author
            it.title = title
            it.tags = tags
            it.uploadDate = uploadDate
        }
    }

    @Test
    fun `assign all variable to new entity and get them`() {
        entity = PhotoEntity(uri, author, title, tags, uploadDate)

        Assert.assertEquals(uri, entity.uri)
        Assert.assertEquals(author, entity.author)
        Assert.assertEquals(title, entity.title)
        Assert.assertEquals(uploadDate, entity.uploadDate)
        tags.forEachIndexed { index, v -> assertEquals(v, entity.tags[index]) }
    }

    @Test
    fun `create a new entity then assign new variables`() {
        verify(entity).uri = uri
        verify(entity).author = author
        verify(entity).title = title
        verify(entity).tags = tags
        verify(entity).uploadDate = uploadDate
    }
}
