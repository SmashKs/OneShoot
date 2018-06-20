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

package smash.ks.com.data.remote.config

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class KsConfigTest {
    private lateinit var config: ApiConfig

    @BeforeTest
    fun setup() {
        config = KsConfig()
    }

    @Test
    fun getApiBaseUrl() {
        val url = config::class.java.getDeclaredField("BASE_URL").apply {
            isAccessible = true
        }

        assertEquals(url.get(config), config.apiBaseUrl)
    }
}
