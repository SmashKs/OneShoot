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

import android.arch.lifecycle.MutableLiveData
import com.devrapid.kotlinknifer.ui
import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.Deferred
import smash.ks.com.domain.datas.KsResponse
import smash.ks.com.domain.datas.KsResponse.Error
import smash.ks.com.domain.datas.KsResponse.Loading
import smash.ks.com.domain.datas.KsResponse.Success
import smash.ks.com.oneshoot.entities.Entity
import smash.ks.com.oneshoot.features.UntilPresenterLiveData

fun <E : Entity, R> MutableLiveData<KsResponse<R>>.requestData(
    usecase: suspend CoroutineScope.() -> Deferred<KsResponse<E>>,
    transformBlock: (E) -> R
) = ui {
    // Opening the loading view.
    value = Loading()
    // Fetching the data from the data layer.
    value = tryResponse {
        val entity = usecase().await()

        entity.data?.let(transformBlock)?.let { Success(it) } ?: Error<R>(msg = "Don't have any response.")
    }
}

fun <E> MutableLiveData<KsResponse<E>>.requestData(usecase: suspend CoroutineScope.() -> Deferred<KsResponse<E>>) = ui {
    // Opening the loading view.
    value = Loading()
    // Fetching the data from the data layer.
    value = tryResponse { usecase().await() }
}

fun UntilPresenterLiveData.requestWithoutResponse(usecase: suspend CoroutineScope.() -> Deferred<KsResponse<Unit>>) =
    ui { value = tryResponse { usecase().await() } }

private inline fun <E> tryResponse(block: () -> KsResponse<E>) = try {
    block()
}
catch (e: Exception) {
    Error<E>(msg = e.message ?: "Unknown error has happened.")
}
