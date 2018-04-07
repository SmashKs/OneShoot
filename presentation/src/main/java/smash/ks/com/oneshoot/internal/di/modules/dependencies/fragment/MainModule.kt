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

import android.arch.lifecycle.ViewModelProvider
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import smash.ks.com.oneshoot.ViewModelFactory
import smash.ks.com.oneshoot.features.main.MainViewModel
import smash.ks.com.oneshoot.internal.di.modules.dependencies.UsecaseModule.usecaseModule

object MainModule {
    fun mainModule() = Kodein.Module {
        import(usecaseModule())

        bind<MainViewModel>() with singleton { MainViewModel(instance(), instance()) }
        bind<ViewModelProvider.Factory>() with singleton {
            ViewModelFactory(instance(),
                             mutableMapOf(MainViewModel::class.java to instance()))
        }
    }
}