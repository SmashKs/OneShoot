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
import com.devrapid.kotlinshaver.isNotNull
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import io.reactivex.Completable
import org.assertj.core.api.Assertions.assertThat
import smash.ks.com.domain.GeneratorFactory.randomLong
import smash.ks.com.domain.GeneratorFactory.randomString
import smash.ks.com.domain.datas.KsData
import smash.ks.com.domain.exceptions.NoParameterException
import smash.ks.com.domain.parameters.KsParam
import smash.ks.com.domain.repositories.DataRepository
import smash.ks.com.domain.usecases.upload.UploadImageToFirebaseUsecase.Requests
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFailsWith

class UploadImageToFirebaseUsecaseTest {
    private lateinit var usecase: UploadImageToFirebaseUsecase
    private lateinit var repository: DataRepository
    private lateinit var data: KsData
    private lateinit var parameter: KsParam

    @BeforeTest
    fun setUp() {
        data = KsData(randomLong, randomString)
        parameter = KsParam(randomLong, randomString, randomString)
        buildUsecaseWithAction(parameter) { completable() }
    }

    @Test
    fun `create the usecase without parameters`() {
        assertFailsWith<NoParameterException> { buildCompletable() }
    }

    @Test
    fun `run through a creating usecase`() {
        buildCompletable(parameter)

        // Assume [retrieveKsImage] was ran once time.
        verify(repository).uploadImage(parameter)
    }

    @Test
    fun `run the case and completed`() {
        buildCompletable(parameter).test().assertComplete()
    }

    @Test
    fun `run the case and let it send on error event`() {
        val exception = Exception("There's something wrong.")

        buildUsecaseWithAction(parameter) { completable { it.onError(exception) } }
        buildCompletable(parameter).test().assertError(exception)
    }

    @Test
    fun `check the rxjava is dispose`() {
        val single = buildCompletable(parameter).test()

        single.dispose()

        assertThat(single.isDisposed).isTrue()
    }

    private fun buildUsecaseWithAction(ksParam: KsParam, returnBlock: (() -> Completable)? = null) {
        repository = mock {
            returnBlock.takeIf { null != it }?.let { on { uploadImage(ksParam) } doReturn it.invoke() }
        }
        usecase = UploadImageToFirebaseUsecase(repository, mock(), mock())
    }

    private fun buildCompletable(ksParam: KsParam? = null) =
        usecase.apply { ksParam?.takeIf(Any::isNotNull)?.let { requestValues = Requests(it) } }.fetchUseCase()
}
