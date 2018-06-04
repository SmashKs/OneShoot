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

package dependenices

/**
 * Central control the dependency libraries. All dependency paths and versions here.
 */
object Deps {
    //region Global
    /**
     * The necessary libs for all modules.
     */
    object Global {
        const val kotlin = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.Kotlin.kotlinLib}"
        const val refelect = "org.jetbrains.kotlin:kotlin-reflect:${Versions.Kotlin.kotlinLib}"
        const val coroutine = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.Kotlin.kotlinCoroutine}"
        const val coroutinesRx2 = "org.jetbrains.kotlinx:kotlinx-coroutines-rx2:${Versions.Kotlin.kotlinCoroutine}"

        const val rxJava2 = "io.reactivex.rxjava2:rxjava:${Versions.RxDep.rxJava2}"
        const val rxKotlin = "io.reactivex.rxjava2:rxkotlin:${Versions.RxDep.rxKotlin2}"

        const val shaver = "com.devrapid.jieyi:kotlinshaver:${Versions.KotlinAndroidExt.shaver}"

        const val kodeinAndroid = "org.kodein.di:kodein-di-framework-android:${Versions.DI.kodein}"
        const val kodenJVM = "org.kodein.di:kodein-di-generic-jvm:${Versions.DI.kodein}"

        const val gson = "com.google.code.gson:gson:${Versions.Parser.gson}"
        const val jsoup = "org.jsoup:jsoup:${Versions.Parser.jsoup}"
    }

    /**
     * The necessary unit test libs for all modules.
     */
    object GlobalTest {
        const val junit = "junit:junit:${Versions.Test.jUnit}"
        const val kotlin = "org.jetbrains.kotlin:kotlin-test-junit:${Versions.Kotlin.kotlinLib}"
        const val mockito = "com.nhaarman:mockito-kotlin:${Versions.Test.mockitoKotlin}"
        const val assertj = "org.assertj:assertj-core:${Versions.Test.assertJ}"
        const val powerMockJunit = "org.powermock:powermock-module-junit4:${Versions.Test.powerMockito}"
        const val powerMockito = "org.powermock:powermock-api-mockito2:${Versions.Test.powerMockito}"
    }

    /**
     * The necessary debug libs for all modules.
     */
    object GlobalDebug {
        const val database = "com.amitshekhar.android:debug-db:${Versions.Database.debug}"
    }
    //endregion

    //region Presentation
    /**
     * The necessary libs only for presentation layer.
     */
    object Presentation {
        const val anko = "org.jetbrains.anko:anko-commons:${Versions.KotlinAndroidExt.anko}"
        const val ankoSdk25 = "org.jetbrains.anko:anko-sdk25:${Versions.KotlinAndroidExt.anko}"
        const val ankoV7 = "org.jetbrains.anko:anko-appcompat-v7:${Versions.KotlinAndroidExt.anko}"
        const val ankoCoroutine = "org.jetbrains.anko:anko-sdk25-coroutines:${Versions.KotlinAndroidExt.anko}"
        const val ankoV7Coroutine = "org.jetbrains.anko:anko-appcompat-v7-coroutines:${Versions.KotlinAndroidExt.anko}"
        const val ktx = "androidx.core:core-ktx:${Versions.KotlinAndroidExt.ktx}"
        const val knifer = "com.devrapid.jieyi:kotlinknifer:${Versions.KotlinAndroidExt.kinfer}"
        const val coroutine = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.Kotlin.kotlinCoroutine}"

        const val lifecycle = "android.arch.lifecycle:extensions:${Versions.KotlinAndroidExt.aac}"

        const val rxBus = "com.hwangjr.rxbus:rxbus:${Versions.RxDep.rxBus}"
        const val rxLifecycle = "com.trello.rxlifecycle2:rxlifecycle-kotlin:${Versions.RxDep.rxLifecycle2}"
        const val rxLifecycleComponent = "com.trello.rxlifecycle2:rxlifecycle-components:${Versions.RxDep.rxLifecycle2}"
        const val rxPermission = "com.tbruyelle.rxpermissions2:rxpermissions:${Versions.RxDep.rxPermission2}"

        const val retrofit2 = "com.squareup.retrofit2:retrofit:${Versions.Network.retrofit2}"
        const val retrofit2Gson = "com.squareup.retrofit2:converter-gson:${Versions.Network.retrofit2}"
        const val retrofit2RxJava2 = "com.squareup.retrofit2:adapter-rxjava2:${Versions.Network.retrofit2}"
        const val okhttpLogging = "com.squareup.okhttp3:logging-interceptor:${Versions.Network.okhttp3}"

        const val glide = "com.github.bumptech.glide:glide:${Versions.Network.glide}"

        const val firebaseCore = "com.google.firebase:firebase-core:${Versions.Firebase.core}"
        const val firebaseDB = "com.google.firebase:firebase-database:${Versions.Firebase.database}"
        const val firebaseAuth = "com.google.firebase:firebase-auth:${Versions.Firebase.auth}"
        const val firebaseMessaging = "com.google.firebase:firebase-messaging:${Versions.Firebase.messaging}"

        const val dbflow = "com.github.Raizlabs.DBFlow:dbflow:${Versions.Database.dbflow}"
        const val dbflowRxJava2KotlinExt =
            "com.github.Raizlabs.DBFlow:dbflow-rx2-kotlinextensions:${Versions.Database.dbflow}"

        const val modelMapper = "org.modelmapper:modelmapper:${Versions.Mapping.modelmapper}"

        const val arv = "com.devrapid.jieyi:adaptiverecyclerview:${Versions.ViewComponent.adaptiveRecyclerView}"

        const val quickDialog = "com.devrapid.jieyi:dialogbuilder:${Versions.Ui.dialog}"

        const val appcompat = "com.android.support:appcompat-v7:${Versions.AndroidComponent.support}"
        const val annot = "com.android.support:support-annotations:${Versions.AndroidComponent.support}"
        const val recyclerview = "com.android.support:recyclerview-v7:${Versions.AndroidComponent.support}"
        const val constraintLayout =
            "com.android.support.constraint:constraint-layout:${Versions.AndroidComponent.contraintLayout}"
    }

    /**
     * The necessary unit test libs only for the presentation layer.
     */
    object PresentationTest {
        const val espressoHelper = "com.github.Zhuinden:espresso-helper:${Versions.Test.espressoHelper}"
        const val robolectric = "org.robolectric:robolectric:${Versions.Test.robolectric}"
        const val runner = "com.android.support.test:runner:${Versions.Test.androidSupportRunner}"
        const val rules = "com.android.support.test:rules:${Versions.Test.androidSupportRules}"
        const val mockito = "org.mockito:mockito-android:${Versions.Test.mockitoAndroid}"
        const val kakao = "com.agoda.kakao:kakao:${Versions.Test.kakao}"
    }
    //endregion

    //region Domain
    /**
     * The necessary libs only for the domain layer.
     */
    object Domain {
        const val rxLifecycle = "com.trello.rxlifecycle2:rxlifecycle-kotlin:${Versions.RxDep.rxLifecycle2}"
    }

    /**
     * The necessary unit test libs only for the domain layer.
     */
    object DomainTest
    //endregion

    //region Data
    /**
     * The necessary libs only for the data layer.
     */
    object Data {
        // Internet
        const val retrofit2 = "com.squareup.retrofit2:retrofit:${Versions.Network.retrofit2}"
        const val retrofit2ConverterGson = "com.squareup.retrofit2:converter-gson:${Versions.Network.retrofit2}"
        const val retrofit2AdapterRxJava2 = "com.squareup.retrofit2:adapter-rxjava2:${Versions.Network.retrofit2}"
        const val okhttp3InterceptorLog = "com.squareup.okhttp3:logging-interceptor:${Versions.Network.okhttp3}"
        // Mapper
        const val mapper = "org.modelmapper:modelmapper:${Versions.Mapping.modelmapper}"
        // Database
        const val dbflowProcessor = "com.github.Raizlabs.DBFlow:dbflow-processor:${Versions.Database.dbflow}"
        const val dbflowCore = "com.github.Raizlabs.DBFlow:dbflow-core:${Versions.Database.dbflow}"
        const val dbflow = "com.github.Raizlabs.DBFlow:dbflow:${Versions.Database.dbflow}"
        const val dbflowKotlinExt = "com.github.Raizlabs.DBFlow:dbflow-kotlinextensions:${Versions.Database.dbflow}"
        const val dbflowRxJava2 = "com.github.Raizlabs.DBFlow:dbflow-rx2:${Versions.Database.dbflow}"
        const val dbflowRxJava2KotlinExt =
            "com.github.Raizlabs.DBFlow:dbflow-rx2-kotlinextensions:${Versions.Database.dbflow}"
        // sql-cipher database encryption (optional)
        const val dbflowSqlEncrypt = "com.github.Raizlabs.DBFlow:dbflow-sqlcipher:${Versions.Database.dbflow}"
        const val dbflowSqlCipher = "net.zetetic:android-database-sqlcipher:${Versions.Database.dbflow}@aar"

        const val firebaseDB = "com.google.firebase:firebase-database:${Versions.Firebase.database}"

        const val appcompat = "com.android.support:appcompat-v7:${Versions.AndroidComponent.support}"
    }

    /**
     * The necessary unit test libs only for the data layer.
     */
    object DataTest {
        const val robolectric = "org.robolectric:robolectric:${Versions.Test.robolectric}"
    }
    //endregion

    //region Ext
    /**
     * The necessary libs only for the extension module.
     */
    object Ext

    /**
     * The necessary unit test libs only for the extension module.
     */
    object ExtTest
    //endregion
}
