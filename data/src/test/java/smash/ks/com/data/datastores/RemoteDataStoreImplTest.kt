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
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.internal.operators.single.SingleJust
import smash.ks.com.data.GeneratorFactory.randomString
import smash.ks.com.data.remote.services.KsFirebase
import smash.ks.com.data.remote.services.KsService
import smash.ks.com.domain.Parameters
import smash.ks.com.domain.exceptions.NoParameterException
import smash.ks.com.domain.parameters.KsParam
import smash.ks.com.domain.parameters.Parameterable
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

    @Test(NoParameterException::class)
    fun `fetch an image without the parameters`() {
        remoteDataStore.getKsImage(null)
    }

    @Test(NoParameterException::class)
    fun `fetch an image with the parameters but toParameter is null`() {
        val parameter = mock<Parameterable> {
            on { toParameter() }.thenReturn(null)
        }

        remoteDataStore.getKsImage(parameter)
    }

    @Test
    fun `fetch an image with the parameters`() {
        val name = randomString
        val parameterMap = mock<Parameters> {
            on { get(KsParam.PARAM_NAME) } doReturn name
        }
        val parameter = mock<Parameterable> {
            on { toParameter() } doReturn parameterMap
        }

        whenever(firebase.retrieveImages(name)).thenReturn(SingleJust(mock()))
        remoteDataStore.getKsImage(parameter)

        verify(firebase).retrieveImages(name)
    }

    @Test(NullPointerException::class)
    fun `fetch an image with the parameters without name variable`() {
        val parameterMap = mock<Parameters> {
            on { get(KsParam.PARAM_NAME) }.thenReturn(null)
        }
        val parameter = mock<Parameterable> {
            on { toParameter() } doReturn parameterMap
        }

        whenever(firebase.retrieveImages(any())).thenReturn(SingleJust(mock()))
        remoteDataStore.getKsImage(parameter)

        verify(firebase).retrieveImages(any())
    }

    @Test(UnsupportedOperationException::class)
    fun `the flow of storing an image to the local database`() {
        remoteDataStore.keepKsImage(mock())
    }

    @Test
    fun `firebase did call the upload an image function`() {
        val parameter = mock<Parameterable>()

        remoteDataStore.pushImageToCloud(parameter)

        verify(firebase).uploadImage(parameter)
    }

    @Test
    fun `firebase did call the analyzing an image tags by ML function`() {
        val parameter = mock<Parameterable>()

        remoteDataStore.analyzeImageTagsByML(parameter)

        verify(firebase).retrieveImageTagsByML()
    }

    @Test
    fun `firebase did call the analyzing an image word content by ML function`() {
        val parameter = mock<Parameterable>()

        remoteDataStore.analyzeImageWordContentByML(parameter)

        verify(firebase).retrieveImageWordContentByML(parameter)
    }
}
