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

package smash.ks.com.oneshoot

import android.app.Application
import org.kodein.Kodein
import org.kodein.KodeinAware
import org.kodein.android.androidModule

/**
 * Android Main Application
 */
class App : Application(), KodeinAware {
//    companion object {
//        lateinit var injector: AndroidInjector<App>
//        val appComponent by lazy { injector as AppComponent }
//    }
//
//    init {
//        // Create an application component injector.
//        injector = DaggerAppComponent.builder().create(this)
//    }
//
//    override fun applicationInjector() = injector

    /**
     * A Kodein Aware class must be within reach of a Kodein object.
     */
    override val kodein: Kodein by Kodein.lazy {
        import(androidModule(this@App))
        /* bindings */
    }
}