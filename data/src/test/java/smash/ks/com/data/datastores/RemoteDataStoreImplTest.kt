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

package smash.ks.com.data.datastores

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import smash.ks.com.data.remote.services.KsFirebase
import smash.ks.com.data.remote.services.KsService
import smash.ks.com.domain.parameters.KsParam
import kotlin.test.BeforeTest
import kotlin.test.Test

/**
 * @author Jieyi Wu
 * @since 2018/06/12
 */
class RemoteDataStoreImplTest {
    private lateinit var remoteDataStore: DataStore
    private lateinit var service: KsService
    private lateinit var firebase: KsFirebase

    @BeforeTest
    fun setUp() {
        service = mock()
        firebase = mock()
        remoteDataStore = RemoteDataStoreImpl(service, firebase)
    }

    @Test
    fun `the flow of fetching an image from the local database`() {
        val param = any<KsParam>()

//        whenever(database.fetchKsData(param)).thenReturn(SingleJust(any()))
//        remoteDataStore.fetchKsImage(param)

//        verify(database).fetchKsData(param)
    }

    @Test(UnsupportedOperationException::class)
    fun `the flow of storing an image to the local database`() {
//        val id = GeneratorFactory.randomLong
//        val uri = GeneratorFactory.randomString
//        val parameter = spy(KsParam(id, GeneratorFactory.randomString, uri))
//
//        whenever(database.keepKsData()).thenReturn(completable())
//        remoteDataStore.keepKsImage(parameter)
//
//        verify(parameter).toParameter()
//        verify(database).keepKsData(id, uri)
    }

    @Test(UnsupportedOperationException::class)
    fun `local data store doesn't support uploadImage method`() {
//        remoteDataStore.uploadImage(mock())
    }

    @Test(UnsupportedOperationException::class)
    fun `local data store doesn't support analyzeImageTagsByML method`() {
//        remoteDataStore.analyzeImageTagsByML(mock())
    }

    @Test(UnsupportedOperationException::class)
    fun `local data store doesn't support analyzeImageWordContentByML method`() {
//        remoteDataStore.analyzeImageWordContentByML(mock())
    }
}
