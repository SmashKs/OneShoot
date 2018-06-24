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

package smash.ks.com.domain.usecases.fake

import com.devrapid.kotlinshaver.isNotNull
import com.devrapid.kotlinshaver.single
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import io.reactivex.Single
import io.reactivex.internal.operators.single.SingleJust
import org.assertj.core.api.Assertions.assertThat
import smash.ks.com.domain.GeneratorFactory.randomLong
import smash.ks.com.domain.GeneratorFactory.randomString
import smash.ks.com.domain.datas.KsData
import smash.ks.com.domain.datas.KsResponse.Success
import smash.ks.com.domain.exceptions.NoParameterException
import smash.ks.com.domain.parameters.KsParam
import smash.ks.com.domain.repositories.DataRepository
import smash.ks.com.domain.usecases.fake.FindKsImageUsecase.Requests
import smash.ks.com.ext.cast
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFailsWith

class FindKsImageUsecaseTest {
    private lateinit var usecase: FindKsImageUsecase
    private lateinit var repository: DataRepository
    private lateinit var data: KsData
    private lateinit var parameter: KsParam

    private val returnDate by lazy { cast<ResponseKsData>(Success(data)) }

    @BeforeTest
    fun setUp() {
        data = KsData(randomLong, randomString)
        parameter = KsParam(randomLong, randomString, randomString)
        buildUsecaseWithAction()
    }

    @Test
    fun `create the usecase without parameters`() {
        assertFailsWith<NoParameterException> { buildSingle() }
    }

    @Test
    fun `run through a creating usecase`() {
        buildSimpleSuccessUsecase()
        buildSingle(parameter)

        // Assume [fetchKsImage] was ran once time.
        verify(repository).fetchKsImage(parameter)
    }

    @Test
    fun `run the case and completed`() {
        buildSimpleSuccessUsecase()
        buildSingle(parameter).test().assertComplete()
    }

    @Test
    fun `run the case and check the return data`() {
        buildSimpleSuccessUsecase()
        buildSingle(parameter).test().assertValue { it.data == returnDate.data }
    }

    @Test
    fun `run the case and check the loading data`() {
        buildSimpleSuccessUsecase()
    }

    @Test
    fun `run the case and let it send on error event`() {
        val exception = Exception("There's something wrong.")

        buildUsecaseWithAction(parameter) { single { it.onError(exception) } }
        buildSingle(parameter).test().assertError(exception)
    }

    @Test
    fun `check the rxjava is dispose`() {
        buildSimpleSuccessUsecase()
        val single = buildSingle(parameter).test()

        single.dispose()

        assertThat(single.isDisposed).isTrue()
    }

    private fun buildUsecaseWithAction(ksParam: KsParam? = null, returnBlock: (() -> Single<KsData>)? = null) {
        repository = mock {
            returnBlock.takeIf { null != it }?.let { on { fetchKsImage(ksParam) } doReturn it.invoke() }
        }
        usecase = FindKsImageUsecase(repository, mock(), mock())
    }

    private fun buildSimpleSuccessUsecase() = buildUsecaseWithAction(parameter) { SingleJust(data) }

    private fun buildSingle(ksParam: KsParam? = null) =
        usecase.apply {
            ksParam?.takeIf(Any::isNotNull)?.let { requestValues = Requests(it) }
        }.fetchUseCase()
}
