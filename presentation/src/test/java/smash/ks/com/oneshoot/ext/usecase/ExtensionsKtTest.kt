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

package smash.ks.com.oneshoot.ext.usecase

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import smash.ks.com.domain.usecases.fake.PersistKsImageUsecase
import kotlin.test.BeforeTest
import kotlin.test.Test

/**
 * @author Jieyi Wu
 * @since 2018/06/20
 */
class ExtensionsKtTest {

    @BeforeTest
    fun setUp() {
    }

    @Test
    fun execute() {
        // Temp test
        val param = mock<PersistKsImageUsecase.Requests>()
        val a = mock<PersistKsImageUsecase>()
        a.ayncCase(param)

        verify(a).requestValues = param
    }

    @Test
    fun execute1() {
    }

    @Test
    fun execute2() {
    }

    @Test
    fun execute3() {
    }

    @Test
    fun execute4() {
    }

    @Test
    fun execute5() {
    }

    @Test
    fun execute6() {
    }

    @Test
    fun execute7() {
    }

    @Test
    fun execute8() {
    }

    @Test
    fun execute9() {
    }

    @Test
    fun execute10() {
    }

    @Test
    fun execute11() {
    }

    @Test
    fun ayncCase() {
    }

    @Test
    fun toAwait() {
    }

    @Test
    fun toAwait1() {
    }

    @Test
    fun ayncCase1() {
    }

    @Test
    fun toAwait2() {
    }

    @Test
    fun toAwait3() {
    }

    @Test
    fun ayncCase2() {
    }

    @Test
    fun toAwait4() {
    }
}
