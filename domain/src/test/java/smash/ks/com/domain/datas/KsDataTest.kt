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

package smash.ks.com.domain.datas

import smash.ks.com.domain.GeneratorFactory.randomLong
import smash.ks.com.domain.GeneratorFactory.randomString
import smash.ks.com.ext.const.DEFAULT_LONG
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * @author Jieyi Wu
 * @since 2018/06/10
 */
class KsDataTest {
    private var id = DEFAULT_LONG
    private lateinit var uri: String
    private lateinit var data: KsData

    @BeforeTest
    fun setUp() {
        id = randomLong
        uri = randomString

        data = KsData(id, uri)
    }

    @Test
    fun `assign the variables into ks data class`() {
        assertEquals(id, data.id)
        assertEquals(uri, data.uri)
    }
}
