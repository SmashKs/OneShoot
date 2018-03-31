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
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.kodein.Kodein
import org.kodein.generic.bind
import org.kodein.generic.instance
import org.kodein.generic.singleton
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object NetModule {
    fun netModule(context: Context) = Kodein.Module {
        bind<Converter.Factory>() with singleton { GsonConverterFactory.create(instance()) }
        bind<CallAdapter.Factory>() with singleton { RxJava2CallAdapterFactory.create() }
        bind<Cache>() with singleton { Cache(context.cacheDir, 10 * 1024 * 1024 /* 10 MiB */) }
        bind<OkHttpClient>() with singleton {
            OkHttpClient.Builder().apply {
                addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                cache(instance())
            }.build()
        }
        bind<Retrofit.Builder>() with singleton {
            Retrofit.Builder().apply {
                addConverterFactory(instance())
                addCallAdapterFactory(instance())
                client(instance())
            }
        }
    }
}