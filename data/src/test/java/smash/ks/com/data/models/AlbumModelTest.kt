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

package smash.ks.com.data.models

import com.nhaarman.mockito_kotlin.verify
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import smash.ks.com.data.GeneratorFactory.randomInt
import smash.ks.com.data.GeneratorFactory.randomString
import smash.ks.com.ext.const.DEFAULT_INT
import kotlin.test.BeforeTest
import kotlin.test.assertEquals

class AlbumModelTest {
    private lateinit var title: String
    private lateinit var author: String
    private var comments = DEFAULT_INT
    private var likes = DEFAULT_INT
    @Mock private lateinit var tags: Tags
    private lateinit var postDate: String
    @Mock private lateinit var uris: Uris

    @Mock private lateinit var model: AlbumModel

    @BeforeTest
    fun setup() {
        MockitoAnnotations.initMocks(this)
        title = randomString
        author = randomString
        comments = randomInt
        likes = randomInt
        postDate = randomString

        model.also {
            it.title = title
            it.author = author
            it.comments = comments
            it.likes = likes
            it.tags = tags
            it.postDate = postDate
            it.uris = uris
        }
    }

    @Test
    fun `assign all variable to new model and get them`() {
        model = AlbumModel(title, author, comments, likes, tags, postDate, uris)

        assertEquals(title, model.title)
        assertEquals(author, model.author)
        assertEquals(comments, model.comments)
        assertEquals(likes, model.likes)
        assertEquals(tags, model.tags)
        assertEquals(postDate, model.postDate)
        assertEquals(uris, model.uris)
    }

    @Test
    fun `create a new model then assign new variables`() {
        verify(model).title = title
        verify(model).author = author
        verify(model).comments = comments
        verify(model).likes = likes
        verify(model).tags = tags
        verify(model).postDate = postDate
        verify(model).uris = uris
    }
}
