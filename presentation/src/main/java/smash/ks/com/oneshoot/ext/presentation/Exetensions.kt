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

@file:Suppress("NOTHING_TO_INLINE")

package smash.ks.com.oneshoot.ext.presentation

import com.devrapid.kotlinknifer.ui
import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.Deferred
import smash.ks.com.domain.models.KsResponse
import smash.ks.com.domain.models.KsResponse.Error
import smash.ks.com.domain.models.KsResponse.Loading
import smash.ks.com.domain.models.KsResponse.Success
import smash.ks.com.oneshoot.entities.Entity
import smash.ks.com.oneshoot.features.UntilPresenterLiveData

/**
 * A transformer wrapper for encapsulating the [ResponseLiveData]'s state
 * changing and the state becomes [Success] when retrieving a data from Data layer by Kotlin coroutine.
 *
 * Also, unboxing the [KsResponse] and obtaining the data inside of the [KsResponse], then return the
 * data to [ResponseLiveData].
 */
fun <E : Entity, R> ResponseLiveData<R>.requestData(
    usecase: suspend CoroutineScope.() -> Deferred<KsResponse<E>>,
    transformBlock: (E) -> R
) = apply {
    ui {
        // Opening the loading view.
        value = Loading()
        // Fetching the data from the data layer.
        value = tryResponse {
            val entity = usecase().await()

            entity.data?.let(transformBlock)?.let { Success(it) } ?: Error<R>(msg = "Don't have any response.")
        }
    }
}

/**
 * A transformer wrapper for encapsulating the [ResponseLiveData]'s state
 * changing and the state becomes [Success] when retrieving a data from Data layer by Kotlin coroutine.
 */
fun <E> ResponseLiveData<E>.requestData(usecase: suspend CoroutineScope.() -> Deferred<KsResponse<E>>) = apply {
    ui {
        // Opening the loading view.
        value = Loading()
        // Fetching the data from the data layer.
        value = tryResponse { usecase().await() }
    }
}

/**
 * In order to run the [RxJava], using the `await`/`async` and informing the View layer.
 */
fun UntilPresenterLiveData.requestWithoutResponse(usecase: suspend CoroutineScope.() -> Deferred<KsResponse<Unit>>) =
    apply { ui { value = tryResponse { usecase().await() } } }

/**
 * Wrapping the `try catch` and ignoring the return value.
 */
private inline fun <E> tryResponse(block: () -> KsResponse<E>) = try {
    block()
}
catch (e: Exception) {
    Error<E>(msg = e.message ?: "Unknown error has happened.")
}
