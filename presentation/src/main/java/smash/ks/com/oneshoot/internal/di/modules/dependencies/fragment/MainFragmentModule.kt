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

import com.trello.rxlifecycle2.components.support.RxFragment
import org.kodein.Kodein
import org.kodein.android.androidScope
import org.kodein.generic.bind
import org.kodein.generic.instance
import org.kodein.generic.scoped
import org.kodein.generic.singleton
import smash.ks.com.oneshoot.features.main.MainFragmentPresenter
import smash.ks.com.oneshoot.internal.di.modules.dependencies.UsecaseModule.usecaseModule
import smash.ks.com.oneshoot.mvp.contracts.MainContract

object MainFragmentModule {
    fun mainFragmentModule() = Kodein.Module {
        import(usecaseModule())

        bind<MainContract.Presenter>() with scoped(androidScope<RxFragment>()).singleton {
            MainFragmentPresenter(instance(), instance())
        }
    }
}