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

import com.ks.smash.ext.internal.di.qulifiers.Local
import com.ks.smash.ext.internal.di.qulifiers.Remote
import dagger.Module
import dagger.Provides
import smash.ks.com.data.datastores.DataStore
import smash.ks.com.data.datastores.LocalDataStoreImpl
import smash.ks.com.data.datastores.RemoteDataStoreImpl
import smash.ks.com.data.local.cache.KsCache
import smash.ks.com.data.local.cache.KsMemoryCache
import smash.ks.com.data.remote.services.KsFirebase
import smash.ks.com.data.remote.services.KsService
import smash.ks.com.data.repositories.DataRepositoryImpl
import smash.ks.com.domain.repositories.DataRepository
import javax.inject.Singleton

@Module
class RepositoryModule {
    @Provides
    @Remote
    @Singleton
    fun provideRemoteRepository(cloudDataStore: RemoteDataStoreImpl): DataStore = cloudDataStore

    @Provides
    @Local
    @Singleton
    fun provideLocalRepository(localStore: LocalDataStoreImpl): DataStore = localStore

    @Provides
    @Local
    @Singleton
    fun provideLocalCache(localCache: KsMemoryCache): KsCache = localCache

    @Provides
    @Singleton
    fun provideRemoteDataStore(ksService: KsService, ksFirebase: KsFirebase) =
        RemoteDataStoreImpl(ksService, ksFirebase)

    @Provides
    @Singleton
    fun providesDataRepository(dataRepository: DataRepositoryImpl): DataRepository = dataRepository
}