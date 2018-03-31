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

import android.content.Context
import org.kodein.Kodein
import org.kodein.generic.bind
import org.kodein.generic.instance
import org.kodein.generic.singleton
import retrofit2.Retrofit
import smash.ks.com.data.remote.RestfulApiFactory
import smash.ks.com.data.remote.config.KsConfig
import smash.ks.com.data.remote.services.KsFirebase
import smash.ks.com.data.remote.services.KsService
import smash.ks.com.oneshoot.external.firebase.v1.KsFirebaseImpl

object ServiceModule {
    fun serviceModule(context: Context) = Kodein.Module {
        import(NetModule.netModule(context))

        bind<KsConfig>() with instance(RestfulApiFactory().createKsConfig())
        bind<KsService>() with singleton {
            with(instance<Retrofit.Builder>()) {
                baseUrl(instance<KsConfig>().apiBaseUrl)
                build()
            }.create(KsService::class.java)
        }
        bind<KsFirebase>() with singleton { KsFirebaseImpl() }
    }
}