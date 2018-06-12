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

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.spy
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.internal.operators.single.SingleJust
import smash.ks.com.data.local.services.KsDatabase
import smash.ks.com.data.models.KsModel
import smash.ks.com.domain.parameters.Parameterable
import kotlin.test.BeforeTest
import kotlin.test.Test

class LocalDataStoreImplTest {
    private lateinit var localDataStore: DataStore
    private lateinit var database: KsDatabase
    private lateinit var model: KsModel
    private lateinit var parameter: Parameterable

    @BeforeTest
    fun setUp() {
        model = mock()
        parameter = mock()
        database = mock()
        localDataStore = LocalDataStoreImpl(database)
    }

    @Test
    fun `the flow of fetching an image from the local database`() {
        whenever(database.fetchKsData(parameter)).thenReturn(SingleJust(model))
        localDataStore.fetchKsImage(parameter)

        verify(database).fetchKsData(parameter)
    }

    @Test
    fun `the flow of storing an image to the local database`() {
        println(database)
        database = spy()
        println(database)

//        whenever(database.keepKsData()).thenReturn(completable())
//
//        verify(parameter).toParameter()
//        verify(localDataStore).keepKsImage(parameter)
    }

    @Test
    fun uploadImage() {
    }

    @Test
    fun analyzeImageTagsByML() {
    }

    @Test
    fun analyzeImageWordContentByML() {
    }
}
