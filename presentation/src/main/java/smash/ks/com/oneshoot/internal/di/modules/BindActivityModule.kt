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

package smash.ks.com.oneshoot.internal.di.modules

import dagger.Module
import dagger.android.ContributesAndroidInjector
import smash.ks.com.oneshoot.features.main.MainActivity
import smash.ks.com.oneshoot.internal.di.scopes.PerActivity

/**
 * Dagger module that provides each of [android.app.Activity] in this central module during the activity lifecycle.
 */
@Module
abstract class BindActivityModule {
    @PerActivity
    @ContributesAndroidInjector(modules = [BindFragmentModule::class])
    abstract fun contributeMainActivityInjector(): MainActivity
}