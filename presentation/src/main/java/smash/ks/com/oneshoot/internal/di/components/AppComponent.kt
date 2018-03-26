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

package smash.ks.com.oneshoot.internal.di.components

import android.content.Context
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import smash.ks.com.oneshoot.App
import smash.ks.com.oneshoot.internal.di.modules.AppModule
import smash.ks.com.oneshoot.internal.di.modules.BindActivityModule
import smash.ks.com.oneshoot.internal.di.modules.NetModule
import smash.ks.com.oneshoot.internal.di.modules.RepositoryModule
import smash.ks.com.oneshoot.internal.di.modules.UtilModule
import javax.inject.Singleton

/**
 * A component whose lifetime is the life of the application.
 */
@Singleton
@Component(modules = [
    AppModule::class,
    BindActivityModule::class,
    RepositoryModule::class,
    NetModule::class,
    UtilModule::class,
    AndroidSupportInjectionModule::class
])
interface AppComponent : AndroidInjector<App> {
    /** [AndroidInjector] Builder for using on this whole app. */
    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<App>()

    /** Providing to dependence components. */
    fun context(): Context

}