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
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import smash.ks.com.data.GeneratorFactory.randomLong
import smash.ks.com.data.GeneratorFactory.randomString
import smash.ks.com.ext.const.DEFAULT_LONG
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class KsDataTest {
    private var id = DEFAULT_LONG
    private lateinit var uri: String
    @Mock private lateinit var data: KsData

    @BeforeTest
    fun setup() {
        MockitoAnnotations.initMocks(this)

        id = randomLong
        uri = randomString

        data.also {
            it.id = id
            it.uri = uri
        }
    }

    @Test
    fun `assign all variable to new data and get them`() {
        data = KsData(id, uri)

        assertEquals(id, data.id)
        assertEquals(uri, data.uri)
    }

    @Test
    fun `create a new data then assign new variables`() {
        verify(data).id = id
        verify(data).uri = uri
    }
}
