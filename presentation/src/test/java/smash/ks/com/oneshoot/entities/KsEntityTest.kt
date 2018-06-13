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

import smash.ks.com.ext.const.DEFAULT_LONG
import smash.ks.com.ext.const.DEFAULT_STR
import smash.ks.com.oneshoot.GeneratorFactory.randomLong
import smash.ks.com.oneshoot.GeneratorFactory.randomString
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class KsEntityTest {
    private var id = DEFAULT_LONG
    private lateinit var uri: String
    private lateinit var entity: KsEntity

    @BeforeTest
    fun setUp() {
        id = randomLong
        uri = randomString

        entity = KsEntity(id, uri)
    }

    @Test
    fun `assign the variables into ks data class`() {
        assertEquals(id, entity.id)
        assertEquals(uri, entity.uri)
    }

    @Test
    fun `create a new object then assign new variables`() {
        entity = KsEntity()
        entity.id = id
        entity.uri = uri

        assertNotEquals(DEFAULT_LONG, entity.id)
        assertNotEquals(DEFAULT_STR, entity.uri)
    }
}
