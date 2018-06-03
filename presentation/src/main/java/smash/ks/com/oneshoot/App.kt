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
import android.content.Context
import com.google.firebase.FirebaseApp
import com.raizlabs.android.dbflow.config.FlowManager
import org.kodein.di.Kodein.Companion.lazy
import org.kodein.di.KodeinAware
import org.kodein.di.android.androidModule
import smash.ks.com.oneshoot.internal.di.modules.AppModule.appModule
import smash.ks.com.oneshoot.internal.di.modules.RecyclerViewModule.recyclerViewModule
import smash.ks.com.oneshoot.internal.di.modules.RepositoryModule.repositoryModule
import smash.ks.com.oneshoot.internal.di.modules.ServiceModule.serviceModule
import smash.ks.com.oneshoot.internal.di.modules.UtilModule.utilModule
import smash.ks.com.oneshoot.internal.di.modules.dependencies.UsecaseModule.usecaseModule

/**
 * Android Main Application
 */
class App : Application(), KodeinAware {
    companion object {
        lateinit var appContext: Context
    }

    init {
        appContext = this
    }

    /**
     * A Kodein Aware class must be within reach of a Kodein object.
     */
    override val kodein = lazy {
        val app = this@App

        import(androidModule(app))
        /** bindings */
        import(appModule())
        import(utilModule(app))
        import(repositoryModule())
        /** usecases are bind here but the scope is depending on each layers.  */
        import(usecaseModule())
        import(serviceModule(app))
        import(recyclerViewModule(app))
    }

    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this)
        FlowManager.init(this)
    }
}
