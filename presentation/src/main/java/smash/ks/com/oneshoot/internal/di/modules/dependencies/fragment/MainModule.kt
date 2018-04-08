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

package smash.ks.com.oneshoot.internal.di.modules.dependencies.fragment

import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.inSet
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import smash.ks.com.domain.usecases.fake.GetKsImageUsecase
import smash.ks.com.oneshoot.features.main.MainViewModel
import smash.ks.com.oneshoot.internal.di.modules.ViewModelEntry
import smash.ks.com.oneshoot.internal.di.modules.dependencies.UsecaseModule.usecaseModule

object MainModule {
    fun mainModule() = Kodein.Module {
        import(usecaseModule())

        bind<ViewModelEntry>().inSet() with provider {
            MainViewModel::class.java to MainViewModel(
                // FIXME(jieyi): 2018/04/08 Here should be instance of the usecase.
//                instance(),
                GetKsImageUsecase(instance(), instance(), instance()),
                instance()
            )
        }
    }
}