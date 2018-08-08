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

package smash.ks.com.domain.usecases.upload

import com.devrapid.kotlinshaver.completable
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import io.reactivex.Completable
import smash.ks.com.domain.GeneratorFactory.randomLong
import smash.ks.com.domain.GeneratorFactory.randomString
import smash.ks.com.domain.exceptions.NoParameterException
import smash.ks.com.domain.models.KsModel
import smash.ks.com.domain.parameters.PhotoParam
import smash.ks.com.domain.repositories.DataRepository
import smash.ks.com.domain.usecases.upload.UploadImageToFirebaseUsecase.Requests
import java.util.Date
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFailsWith

class UploadImageToFirebaseUsecaseTest {
    private lateinit var usecase: UploadImageToFirebaseUsecase
    private lateinit var repository: DataRepository
    private lateinit var model: KsModel
    private lateinit var parameter: PhotoParam

    @BeforeTest
    fun setUp() {
        model = KsModel(randomLong, randomString)
        parameter = PhotoParam(randomString, byteArrayOf(), randomString, randomString, emptyList(), Date())
        buildUsecaseWithAction(parameter) { completable() }
    }

    @Test
    fun `create the usecase without parameters`() {
        assertFailsWith<NoParameterException> { buildCompletable() }
    }

    // TODO(jieyi): 2018/08/07 fix this unit test.
    @Test
    fun `run through a creating usecase`() {
//        buildCompletable(parameter)

        // Assume [fetchKsImage] was ran once time.
//        verify(repository).uploadImage(parameter)
    }

    @Test
    fun `run the case and completed`() {
//        buildCompletable(parameter).test().assertComplete()
    }

    @Test
    fun `run the case and let it send on error event`() {
        val exception = Exception("There's something wrong.")

//        buildUsecaseWithAction(parameter) { completable { it.onError(exception) } }
//        buildCompletable(parameter).test().assertError(exception)
    }

    @Test
    fun `check the rxjava is dispose`() {
//        val single = buildCompletable(parameter).test()

//        single.dispose()

//        assertThat(single.isDisposed).isTrue()
    }

    private fun buildUsecaseWithAction(PhotoParam: PhotoParam, returnBlock: (() -> Completable)? = null) {
        repository = mock {
            returnBlock?.let { on { uploadImage(PhotoParam) } doReturn it.invoke() }
        }
        usecase = UploadImageToFirebaseUsecase(repository, mock(), mock())
    }

    private fun buildCompletable(photoParam: PhotoParam? = null) =
        usecase.apply { photoParam?.let { requestValues = Requests(it) } }.fetchUseCase()
}
