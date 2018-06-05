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

package smash.ks.com.oneshoot.ext.aac

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import kotlinx.coroutines.experimental.Deferred
import smash.ks.com.domain.datas.KsResponse
import smash.ks.com.domain.datas.KsResponse.Error
import smash.ks.com.domain.datas.KsResponse.Loading
import smash.ks.com.domain.datas.KsResponse.Success
import smash.ks.com.domain.exceptions.NoParameterException
import smash.ks.com.oneshoot.bases.LoadView

/**
 * Observe the [LiveData]'s nullable value from [android.arch.lifecycle.ViewModel].
 */
inline fun <reified T> LifecycleOwner.observe(liveData: LiveData<T>, noinline block: (T?) -> Unit) =
    liveData.observe(this, Observer(block))

/**
 * Observe the [LiveData]'s nonnull value from [android.arch.lifecycle.ViewModel].
 */
inline fun <reified T> LifecycleOwner.observeNonNull(liveData: LiveData<T>, noinline block: (T) -> Unit) =
    liveData.observe(this, Observer { it?.let(block) })

/**
 * Observe the [LiveData]'s nullable value which comes from the un-boxed [KsResponse] value
 * from [android.arch.lifecycle.ViewModel].
 */
inline fun <reified E, T : KsResponse<E>> LifecycleOwner.observeUnbox(
    liveData: LiveData<T>,
    noinline block: (E?) -> Unit
) = liveData.observe(this, Observer { it?.data.let(block) })

/**
 * Observe the [LiveData]'s nonnull value which comes from the un-boxed [KsResponse] value
 * from [android.arch.lifecycle.ViewModel].
 */
inline fun <reified E, T : KsResponse<E>> LifecycleOwner.observeUnboxNonNull(
    liveData: LiveData<T>,
    noinline block: (E) -> Unit
) = liveData.observe(this, Observer { it?.data?.let(block) })

/**
 * Check the [KsResponse]'s changing and do the corresponding reaction.Here're three data
 * type [Loading], [Success], and [Error].
 *
 * - [Loading] state will show the loading view.
 * - [Success] state will extract the data from the inside class to be used [successBlock].
 * - [Error] state will show the error view and msg to the user.
 */
fun <D> LoadView.peelResponse(response: KsResponse<D>, successBlock: (D) -> Unit) =
    response.also {
        when (it) {
            is Loading<*> -> showLoading()
            is Success<D> -> {
                it.data?.let(successBlock) ?: throw NoParameterException("There's no any parameters.")
                hideLoading()
            }
            is Error<*> -> {
                hideLoading()
                showError(it.msg)
            }
        }
    }

inline fun <T> Deferred<T>?.abort(cause: Throwable? = null) =
    takeIf { null != it && it.isActive }?.cancel(cause)
