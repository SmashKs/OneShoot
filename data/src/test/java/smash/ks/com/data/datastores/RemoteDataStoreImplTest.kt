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

import com.devrapid.kotlinshaver.single
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import smash.ks.com.data.GeneratorFactory.randomString
import smash.ks.com.data.remote.services.KsCloudinary
import smash.ks.com.data.remote.services.KsFirebase
import smash.ks.com.data.remote.services.KsService
import smash.ks.com.domain.AnyParameters
import smash.ks.com.domain.Parameters
import smash.ks.com.domain.exceptions.NoParameterException
import smash.ks.com.domain.parameters.KsAnalyzeImageParam
import smash.ks.com.domain.parameters.KsParam
import smash.ks.com.domain.parameters.Parameterable
import kotlin.test.BeforeTest
import kotlin.test.Test

class RemoteDataStoreImplTest {
    @Mock private lateinit var service: KsService
    @Mock private lateinit var firebase: KsFirebase
    @Mock private lateinit var cloudinary: KsCloudinary
    @InjectMocks private lateinit var remoteDataStore: RemoteDataStoreImpl

    @BeforeTest
    fun setUp() {
        MockitoAnnotations.initMocks(this)
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
        val parameter = mock<Parameterable> {
            on { toParameter() } doReturn mock<Parameters>()
            on { toParameter()[KsParam.PARAM_NAME] } doReturn name
        }

        whenever(firebase.retrieveImages(name)).thenReturn(single(mock()))
        remoteDataStore.getKsImage(parameter)

        verify(firebase).retrieveImages(name)
    }

    @Test(NullPointerException::class)
    fun `fetch an image with the parameters without name variable`() {
        val parameter = mock<Parameterable> {
            on { toParameter() } doReturn mock<Parameters>()
            on { toParameter()[KsParam.PARAM_NAME] }.thenReturn(null)
        }

        whenever(firebase.retrieveImages(any())).thenReturn(single(mock()))
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

        remoteDataStore.pushImageToFirebase(parameter)

        verify(firebase).uploadImage(parameter)
    }

    @Test(NullPointerException::class)
    fun `with empty parameter firebase did call the analyzing an image tags by ML function`() {
        remoteDataStore.analyzeImageTagsByML(mock())
    }

    @Test(ClassCastException::class)
    fun `with wrong parameters firebase did call the analyzing an image tags by ML function`() {
        val parameter = mock<Parameterable> {
            on { toAnyParameter() } doReturn mock<AnyParameters>()
            on { toAnyParameter()[KsAnalyzeImageParam.PARAM_BYTE_ARRAY] } doReturn "This is one shoot"
        }

        remoteDataStore.analyzeImageTagsByML(parameter)
    }

    @Test
    fun `firebase did call the analyzing an image tags by ML function`() {
        val byteArray = byteArrayOf()

        val parameter = mock<Parameterable> {
            on { toAnyParameter() } doReturn mock<AnyParameters>()
            on { toAnyParameter()[KsAnalyzeImageParam.PARAM_BYTE_ARRAY] } doReturn byteArray
        }

        whenever(firebase.retrieveImageTagsByML(byteArray)).thenReturn(single(mock()))
        remoteDataStore.analyzeImageTagsByML(parameter)

        verify(firebase).retrieveImageTagsByML(byteArray)
    }

    @Test
    fun `firebase did call the analyzing an image word content by ML function`() {
        val parameter = mock<Parameterable>()

        remoteDataStore.analyzeImageWordContentByML(parameter)

        verify(firebase).retrieveImageWordContentByML(parameter)
    }
}
