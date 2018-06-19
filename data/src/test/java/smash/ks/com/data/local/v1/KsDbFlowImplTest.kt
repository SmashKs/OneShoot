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

package smash.ks.com.data.local.v1

import com.devrapid.kotlinshaver.completable
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import smash.ks.com.data.local.services.KsDatabase
import smash.ks.com.ext.const.UniqueId
import kotlin.test.BeforeTest
import kotlin.test.Test

class KsDbFlowImplTest {
    private lateinit var database: KsDatabase

    @BeforeTest
    fun setUp() {
        database = mock()
    }

    @Test
    fun retrieveKsData() {
    }

    @Test
    fun storeKsData() {
        val id = any<UniqueId>()
        val uri = any<String>()

        whenever(database.storeKsData(id, uri)) doReturn completable()
        database.storeKsData(id, uri)
    }
}
