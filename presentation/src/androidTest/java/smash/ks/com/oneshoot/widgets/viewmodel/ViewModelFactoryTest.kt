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

package smash.ks.com.oneshoot.widgets.viewmodel

import androidx.lifecycle.ViewModel
import io.mockk.mockk
import smash.ks.com.oneshoot.App
import smash.ks.com.oneshoot.features.fake.FakeViewModel
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class ViewModelFactoryTest {
    private lateinit var vm: ViewModel
    private lateinit var factory: ViewModelFactory
    private lateinit var viewModels: LookUpViewModel

    @BeforeTest
    fun setUp() {
        vm = mockk()
        viewModels = mutableMapOf()
        factory = ViewModelFactory(App(), viewModels)
    }

    @Test
    fun createVMAndGetCorrect() {
        viewModels[vm::class.java] = vm

        assertEquals(vm, factory.create(vm::class.java))
    }

    @Test
    fun createVMWithWrongTypeVM() {
        val another = mockk<FakeViewModel>()

        viewModels[vm::class.java] = vm

        assertNotEquals(another, factory.create(vm::class.java))
    }

    @Test(expected = TypeCastException::class)
    fun createVMWithEmptyMap() {
        assertEquals(vm, factory.create(vm::class.java))
    }

    @Test(expected = AssertionError::class)
    fun createVMWithWrongType() {
        val another = mockk<FakeViewModel>()

        viewModels[vm::class.java] = another

        assertEquals(vm, factory.create(vm::class.java))
    }
}
