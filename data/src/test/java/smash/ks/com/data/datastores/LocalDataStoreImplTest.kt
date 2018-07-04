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

import com.devrapid.kotlinshaver.completable
import com.devrapid.kotlinshaver.single
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.doThrow
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.spy
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import smash.ks.com.data.GeneratorFactory.randomLong
import smash.ks.com.data.GeneratorFactory.randomString
import smash.ks.com.data.local.services.KsDatabase
import smash.ks.com.data.local.services.KsFlow
import smash.ks.com.domain.AnyParameters
import smash.ks.com.domain.Parameters
import smash.ks.com.domain.exceptions.NoParameterException
import smash.ks.com.domain.parameters.KsAnalyzeImageParam
import smash.ks.com.domain.parameters.KsParam
import smash.ks.com.domain.parameters.Parameterable
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFailsWith

class LocalDataStoreImplTest {
    @Mock private lateinit var database: KsDatabase
    @Mock private lateinit var flow: KsFlow
    @InjectMocks private lateinit var localDataStore: LocalDataStoreImpl

    @BeforeTest
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `the flow of fetching an image from the local database without parameter`() {
        whenever(database.retrieveKsData(any())).thenReturn(single(mock()))

        assertFailsWith<NoParameterException> { localDataStore.getKsImage(null) }

        val parameter = mock<Parameterable> { on { toParameter() }.thenReturn(null) }

        assertFailsWith<NoParameterException> { localDataStore.getKsImage(parameter) }
    }

    @Test
    fun `the flow of fetching an image from the local database`() {
        val id = randomLong
        val param = mock<Parameterable> {
            on { toParameter() } doReturn mock<Parameters>()
            on { toParameter()[KsParam.PARAM_ID] } doReturn id.toString()
        }

        whenever(database.retrieveKsData(id)).thenReturn(single(mock()))
        localDataStore.getKsImage(param)

        verify(database).retrieveKsData(id)
    }

    @Test
    fun `the flow of fetching an image from the local database without id`() {
        val param = mock<Parameterable> {
            on { toParameter() } doReturn mock<Parameters>()
            on { toParameter()[KsParam.PARAM_ID] }.thenReturn(null)
        }

        whenever(database.retrieveKsData(null)).thenReturn(single(mock()))
        localDataStore.getKsImage(param)

        verify(database).retrieveKsData(null)
    }

    @Test
    fun `the flow of storing an image to the local database`() {
        val id = randomLong
        val uri = randomString
        val parameter = spy(KsParam(id, randomString, uri))

        whenever(database.storeKsData()).thenReturn(completable())
        localDataStore.keepKsImage(parameter)

        verify(parameter).toParameter()
        verify(database).storeKsData(id, uri)
    }

    @Test(NullPointerException::class)
    fun `keep ks data's toParameter is a null hash map`() {
        val parameter = mock<KsParam> {
            on { toParameter() } doReturn mock<Parameters>()
            on { toParameter()[KsParam.PARAM_ID] } doThrow NullPointerException()
            on { toParameter()[KsParam.PARAM_URI] } doThrow NullPointerException()
        }

        whenever(database.storeKsData()).thenReturn(mock())
        localDataStore.keepKsImage(parameter)

        verify(database).storeKsData(any(), any())
    }

    @Test(UnsupportedOperationException::class)
    fun `local data store doesn't support uploadImage method`() {
        localDataStore.pushImageToCloud(mock())
    }

    @Test(NullPointerException::class)
    fun `analyzeImageTagsByML with empty parameter calling`() {
        localDataStore.analyzeImageTagsByML(mock())
    }

    @Test(ClassCastException::class)
    fun `analyzeImageTagsByML with wrong type parameter calling`() {
        val parameter = mock<Parameterable> {
            on { toAnyParameter() } doReturn mock<AnyParameters>()
            on { toAnyParameter()[KsAnalyzeImageParam.PARAM_BYTE_ARRAY] } doReturn "This is one shoot"
        }

        localDataStore.analyzeImageTagsByML(parameter)
    }

    @Test
    fun `the flow of analyzeImageTagsByML method`() {
        val byteArray = byteArrayOf()
        val parameter = mock<Parameterable> {
            on { toAnyParameter() } doReturn mock<AnyParameters>()
            on { toAnyParameter()[KsAnalyzeImageParam.PARAM_BYTE_ARRAY] } doReturn byteArray
        }

        whenever(flow.retrieveImageTagsByML(byteArray)).thenReturn(single(mock()))
        localDataStore.analyzeImageTagsByML(parameter)

        verify(flow).retrieveImageTagsByML(byteArray)
    }

    @Test
    fun `the flow of analyzeImageWordContentByML method`() {
        val parameter = mock<Parameterable>()

        whenever(flow.retrieveImageWordContentByML(parameter)).thenReturn(single(mock()))
        localDataStore.analyzeImageWordContentByML(parameter)

        verify(flow).retrieveImageWordContentByML(parameter)
    }
}
