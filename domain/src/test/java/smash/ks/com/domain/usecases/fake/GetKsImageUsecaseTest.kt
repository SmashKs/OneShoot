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

import com.nhaarman.mockito_kotlin.mock
import smash.ks.com.domain.executors.PostExecutionThread
import smash.ks.com.domain.executors.ThreadExecutor
import smash.ks.com.domain.repositories.DataRepository
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFailsWith

class GetKsImageUsecaseTest {
    private lateinit var usecase: GetKsImageUsecase
    private lateinit var repository: DataRepository
    private lateinit var threadExecutor: ThreadExecutor
    private lateinit var postExecutionThread: PostExecutionThread

    @BeforeTest
    fun setUp() {
        repository = mock()
        threadExecutor = mock()
        postExecutionThread = mock()
        usecase = GetKsImageUsecase(repository, threadExecutor, postExecutionThread)
    }

    @Test
    fun checkWithoutParameters() {
        assertFailsWith<Exception> { usecase.fetchUseCase() }
    }

    @Test
    fun checkWithParameters() {
//        val param = Requests(mock())
//        usecase.apply { requestValues = param }
//
//        assertThat(usecase.fetchUseCase()).isInstanceOf(Single::class.java)
    }
}