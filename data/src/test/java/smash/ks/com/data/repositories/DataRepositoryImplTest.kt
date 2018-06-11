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

package smash.ks.com.data.repositories

import com.devrapid.kotlinshaver.completable
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import smash.ks.com.data.GeneratorFactory.randomLong
import smash.ks.com.data.GeneratorFactory.randomString
import smash.ks.com.data.datastores.DataStore
import smash.ks.com.domain.parameters.KsParam
import smash.ks.com.domain.parameters.Parameterable
import kotlin.test.BeforeTest
import kotlin.test.Test

class DataRepositoryImplTest {
    private val parameter: Parameterable get() = KsParam(randomLong, randomString, randomString)
    private lateinit var local: DataStore
    private lateinit var remote: DataStore
    private lateinit var dataRepositoryImpl: DataRepositoryImpl

    @BeforeTest
    fun setup() {
        local = mock()
        remote = mock()
        dataRepositoryImpl = DataRepositoryImpl(mock(), local, remote, mock())
    }

    @Test
    fun retrieveKsImage() {
    }

    @Test
    fun storeKsImage() {
    }

    @Test
    fun `check upload an image`() {
        val param = parameter

        whenever(remote.uploadImage(param)).thenReturn(completable())
        dataRepositoryImpl.uploadImage(param)

        verify(remote).uploadImage(param)
    }

    @Test
    fun retrieveImageTagsByML() {
    }

    @Test
    fun retrieveImageWordContentByML() {
    }
}
