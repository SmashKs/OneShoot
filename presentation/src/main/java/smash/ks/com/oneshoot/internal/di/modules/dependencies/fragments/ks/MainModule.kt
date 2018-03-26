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

package smash.ks.com.oneshoot.internal.di.modules.dependencies.fragments.ks

import dagger.Module
import dagger.Provides
import smash.ks.com.domain.usecases.GetKsImageCase
import smash.ks.com.oneshoot.features.main.MainFragmentPresenter
import smash.ks.com.oneshoot.internal.di.modules.dependencies.fragments.UsecaseModule
import smash.ks.com.oneshoot.internal.di.scopes.PerFragment
import smash.ks.com.oneshoot.mvp.contracts.MainContract

@Module(includes = [UsecaseModule::class])
class MainModule {
    @Provides
    @PerFragment
    fun provideMainPresenter(getKsImageCase: GetKsImageCase): MainContract.Presenter =
        MainFragmentPresenter(getKsImageCase)
}