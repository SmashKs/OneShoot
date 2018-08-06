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
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import smash.ks.com.data.GeneratorFactory.randomLong
import smash.ks.com.data.GeneratorFactory.randomString
import smash.ks.com.data.datas.MapperPool
import smash.ks.com.data.datastores.DataStore
import smash.ks.com.domain.parameters.KsParam
import smash.ks.com.domain.parameters.Parameterable
import kotlin.test.BeforeTest
import kotlin.test.Test

class DataRepositoryImplTest {
    private val parameter: Parameterable get() = KsParam(randomLong, randomString, randomString)
    @Mock private lateinit var local: DataStore
    @Mock private lateinit var remote: DataStore
    private lateinit var dataRepository: DataRepositoryImpl
    private lateinit var mapperPool: MapperPool

    @BeforeTest
    fun setup() {
        MockitoAnnotations.initMocks(this)
        mapperPool = mock()
        dataRepository = DataRepositoryImpl(mock(), local, remote, mapperPool)
    }

    @Test
    fun `normal flow for fetching a ks image`() {
    }

    @Test
    fun storeKsImage() {
    }

    @Test
    fun `check upload an image`() {
        val param = parameter

        whenever(remote.pushImageToFirebase(param)).thenReturn(completable())
        dataRepository.uploadImage(param)

        verify(remote).pushImageToFirebase(param)
    }

    @Test
    fun retrieveImageTagsByML() {
    }

    @Test
    fun retrieveImageWordContentByML() {
    }
}
