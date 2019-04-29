/*
 * Copyright (C) 2019 The Smash Ks Open Project
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

import dependenices.Versions.AndroidComponent.androidx

/**
 * Collect all libs of the version number.
 */
object Versions {
    /**
     * Gradle android version.
     */
    object Android {
        const val minSdk = 21
        const val targetSdk = 28
        const val buiildTool = "28.0.2"
        const val compileSdk = targetSdk
    }

    /**
     * Related Android component lib version.
     */
    object AndroidComponent {
        const val androidx = "1.0.0"
        const val appcompat = "1.1.0-alpha04"
        const val annotation = "1.1.0-beta01"
        const val material = "1.1.0-alpha05"
        const val recyclerview = "1.1.0-alpha04"
        const val cardview = androidx
        const val constraintLayout = "2.0.0-alpha5"
        const val coordinatorLayout = "1.1.0-alpha01"
        const val navigation = "2.1.0-alpha02"
    }

    /**
     * Related Kotlin lib version.
     */
    object Kotlin {
        const val kotlinLib = "1.3.31"
        const val kotlinCoroutine = "1.2.1"
    }

    /**
     * Related view component lib version.
     */
    object ViewComponent {
        const val adaptiveRecyclerView = "1.0.16"
    }

    /**
     * Related Kotlin extensions lib version.
     */
    object KotlinAndroidExt {
        const val dex = "2.0.1"
        const val aac = androidx
        const val aacLifecycle = "2.1.0-alpha04"
        const val anko = "0.10.8"
        const val ktx = "1.1.0-alpha05"
        const val kinfer = "2.1.10"
    }

    /**
     * Related dependency injection lib version.
     */
    object DI {
        const val kodein = "5.3.0"
    }

    /**
     * Related Firebase lib version.
     */
    object Firebase {
        const val core = "16.0.8"
        const val database = "16.1.0"
        const val auth = "16.0.3"
        const val messaging = "17.3.0"

        const val mlVision = "19.0.3"
        const val mlImageLabel = "17.0.2"
    }

    object TensorFlow {
        const val lite = "1.13.1"
    }

    object CloudStore {
        const val cloudinary = "1.26.0"
    }

    /**
     * Related database lib version.
     */
    object Database {
        const val dbflow = "4.2.4"
        const val debug = "1.0.6"
    }

    /**
     * Related network lib version.
     */
    object Network {
        const val glide = "4.9.0"
        const val retrofit2 = "2.5.0"
        const val okhttp3 = "3.14.1"
    }

    /**
     * Related reactive lib version.
     */
    object RxDep {
        const val rxJava2 = "2.2.8"
        const val rxKotlin2 = "2.3.0"
        const val rxLifecycle2 = "2.2.2"
        const val rxPermission2 = "0.9.5@aar"
        const val rxBus = "2.0.1"
    }

    /**
     * Related parser lib version.
     */
    object Parser {
        const val gson = "2.8.5"
        const val jsoup = "1.10.3"
    }

    /**
     * Related mapping lib version.
     */
    object Mapping {
        const val modelmapper = "2.3.3"
    }

    /**
     * Related Android UI lib version.
     */
    object Ui {
        const val dialog = "1.0.7"
        const val loading = "1.3.0"
        const val materialChip = "1.0.8"
        const val cameraView = "2.0.0-beta04"
    }

    /**
     * Related Android unit test lib version.
     */
    object Test {
        const val jUnit = "4.13-beta-2"
        const val espressoHelper = "0.1.3"
        const val kakao = "2.0.0"
        const val robolectric = "3.4.2"
        const val assertJ = "3.12.2"
        const val powerMockito = "2.0.2"
        const val mockitoKotlin = "1.6.0"
        const val mockitoAndroid = "2.9.0"
        const val mockk = "1.9.3.kotlin12"
        const val byteBuddy = "1.9.12"
    }
}
