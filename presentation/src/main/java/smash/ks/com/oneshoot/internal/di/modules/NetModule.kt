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
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import smash.ks.com.data.remote.RestfulApiFactory
import smash.ks.com.data.remote.services.KsService
import javax.inject.Singleton

@Module
class NetModule {
    @Provides
    @Singleton
    fun provideConverterGson(gson: Gson) = GsonConverterFactory.create(gson)

    @Provides
    @Singleton
    fun provideRxJavaCallAdapter() = RxJava2CallAdapterFactory.create()

    @Provides
    @Singleton
    fun provideGson() = with(GsonBuilder()) {
        setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        setLenient()
        create()
    }

    @Provides
    @Singleton
    fun provideOkHttpCache(context: Context) = Cache(context.cacheDir, 10 * 1024 * 1024 /* 10 MiB */)

    @Provides
    @Singleton
    fun provideOkHttpClient(cache: Cache) = OkHttpClient.Builder().apply {
        addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        cache(cache)
    }.build()

    @Provides
    @Singleton
    fun provideBaseRetrofitBuilder(
        converter: GsonConverterFactory,
        callAdapter: RxJava2CallAdapterFactory,
        okHttpClient: OkHttpClient
    ) = Retrofit.Builder().apply {
        addConverterFactory(converter)
        addCallAdapterFactory(callAdapter)
        client(okHttpClient)
    }

    @Provides
    @Singleton
    fun provideKaritokeSerivce(baseBuilder: Retrofit.Builder, restfulApiFactory: RestfulApiFactory) =
        with(baseBuilder) {
            baseUrl(restfulApiFactory.createKaritokeConfig().apiBaseUrl)
            build()
        }.create(KsService::class.java)
}