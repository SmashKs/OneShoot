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

package smash.ks.com.domain.models

import smash.ks.com.domain.GeneratorFactory.randomLong
import smash.ks.com.domain.GeneratorFactory.randomString
import smash.ks.com.ext.const.DEFAULT_LONG
import smash.ks.com.ext.const.DEFAULT_STR
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class KsModelTest {
    private var id = DEFAULT_LONG
    private lateinit var uri: String
    private lateinit var model: KsModel

    @BeforeTest
    fun setUp() {
        id = randomLong
        uri = randomString

        model = KsModel(id, uri)
    }

    @Test
    fun `assign the variables into ks model class`() {
        assertEquals(id, model.id)
        assertEquals(uri, model.uri)
    }

    @Test
    fun `create a new model then assign new variables`() {
        model = KsModel()
        model.id = id
        model.uri = uri

        assertNotEquals(DEFAULT_LONG, model.id)
        assertNotEquals(DEFAULT_STR, model.uri)
    }
}
