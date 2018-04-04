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

package smash.ks.com.oneshoot.internal.di.modules.dependencies

import android.support.v4.app.FragmentActivity
import org.kodein.di.Kodein
import org.kodein.di.android.androidScope
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.scoped
import org.kodein.di.generic.singleton
import smash.ks.com.domain.usecases.GetKsImageCase
import smash.ks.com.domain.usecases.fake.GetKsImageUsecase

object UsecaseModule {
    fun usecaseModule() = Kodein.Module {
        bind<GetKsImageCase>() with scoped(androidScope<FragmentActivity>()).singleton {
            GetKsImageUsecase(instance(), instance(), instance())
        }
    }
}