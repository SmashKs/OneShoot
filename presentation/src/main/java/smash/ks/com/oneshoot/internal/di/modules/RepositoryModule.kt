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

import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import smash.ks.com.data.datastores.DataStore
import smash.ks.com.data.datastores.LocalDataStoreImpl
import smash.ks.com.data.datastores.RemoteDataStoreImpl
import smash.ks.com.data.local.cache.KsCache
import smash.ks.com.data.local.cache.KsMemoryCache
import smash.ks.com.data.repositories.DataRepositoryImpl
import smash.ks.com.domain.repositories.DataRepository
import smash.ks.com.oneshoot.internal.di.tag.KsTag.LOCAL
import smash.ks.com.oneshoot.internal.di.tag.KsTag.REMOTE

object RepositoryModule {
    fun repositoryModule() = Kodein.Module {
        bind<KsCache>(LOCAL) with singleton { KsMemoryCache() }
        bind<DataStore>(REMOTE) with singleton { RemoteDataStoreImpl(instance(), instance()) }
        bind<DataStore>(LOCAL) with singleton { LocalDataStoreImpl() }
        bind<DataRepository>() with singleton {
            DataRepositoryImpl(instance(LOCAL), instance(LOCAL), instance(REMOTE), instance())
        }
    }
}