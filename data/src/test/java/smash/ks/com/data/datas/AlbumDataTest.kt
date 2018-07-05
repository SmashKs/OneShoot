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
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import smash.ks.com.data.GeneratorFactory.randomInt
import smash.ks.com.data.GeneratorFactory.randomString
import smash.ks.com.ext.const.DEFAULT_INT
import kotlin.test.BeforeTest
import kotlin.test.assertEquals

class AlbumDataTest {
    private lateinit var title: String
    private lateinit var author: String
    private var comments = DEFAULT_INT
    private var likes = DEFAULT_INT
    @Mock private lateinit var tags: Tags
    private lateinit var postDate: String
    @Mock private lateinit var uris: Uris

    @Mock private lateinit var data: AlbumData

    @BeforeTest
    fun setup() {
        MockitoAnnotations.initMocks(this)
        title = randomString
        author = randomString
        comments = randomInt
        likes = randomInt
        postDate = randomString

        data.also {
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
    fun `assign all variable to new data and get them`() {
        data = AlbumData(title, author, comments, likes, tags, postDate, uris)

        assertEquals(title, data.title)
        assertEquals(author, data.author)
        assertEquals(comments, data.comments)
        assertEquals(likes, data.likes)
        assertEquals(tags, data.tags)
        assertEquals(postDate, data.postDate)
        assertEquals(uris, data.uris)
    }

    @Test
    fun `create a new data then assign new variables`() {
        verify(data).title = title
        verify(data).author = author
        verify(data).comments = comments
        verify(data).likes = likes
        verify(data).tags = tags
        verify(data).postDate = postDate
        verify(data).uris = uris
    }
}
