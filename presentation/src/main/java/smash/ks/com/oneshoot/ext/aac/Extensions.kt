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

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.devrapid.kotlinshaver.isNull
import kotlinx.coroutines.experimental.Deferred
import smash.ks.com.domain.models.response.KsResponse
import smash.ks.com.domain.models.response.KsResponse.Error
import smash.ks.com.domain.models.response.KsResponse.Loading
import smash.ks.com.domain.models.response.KsResponse.Success
import smash.ks.com.oneshoot.bases.LoadView

/**
 * Observe the [LiveData]'s nullable value from [androidx.lifecycle.ViewModel].
 */
inline fun <reified T> LifecycleOwner.observe(liveData: LiveData<T>, noinline block: ((T?) -> Unit)? = null) =
    block?.let { liveData.observe(this, Observer(it)) }

/**
 * Observe the [LiveData]'s nonnull value from [androidx.lifecycle.ViewModel].
 */
inline fun <reified T> LifecycleOwner.observeNonNull(liveData: LiveData<T>, noinline block: ((T) -> Unit)? = null) =
    block?.run { liveData.observe(this@observeNonNull, Observer { it?.let(this) }) }

/**
 * Observe the [LiveData]'s nullable value which comes from the un-boxed [KsResponse] value
 * from [androidx.lifecycle.ViewModel].
 */
inline fun <reified E, T : KsResponse<E>> LifecycleOwner.observeUnbox(
    liveData: LiveData<T>,
    noinline block: ((E?) -> Unit)? = null
) = block?.run { liveData.observe(this@observeUnbox, Observer { it?.data.let(this) }) }

/**
 * Observe the [LiveData]'s nonnull value which comes from the un-boxed [KsResponse] value
 * from [androidx.lifecycle.ViewModel].
 */
inline fun <reified E, T : KsResponse<E>> LifecycleOwner.observeUnboxNonNull(
    liveData: LiveData<T>,
    noinline block: ((E) -> Unit)? = null
) = block?.run { liveData.observe(this@observeUnboxNonNull, Observer { it?.data?.let(block) }) }

/**
 * Check the [KsResponse]'s changing and do the corresponding reaction.
 */
fun <D> LoadView.peelResponse(
    response: KsResponse<D>,
    errorBlock: ((String) -> Unit)? = null,
    successBlock: ((D) -> Unit)? = null
) = peelResponseOptions(response,
                        isShowError = errorBlock.isNull(),
                        errorBlock = errorBlock,
                        successBlock = successBlock)

/**
 * Check the [KsResponse]'s changing and do the corresponding reaction. For the situation which we need
 * to use multi-requests in the same time.
 */
fun <D> LoadView.peelResponseAndContinue(response: KsResponse<D>, successBlock: ((D) -> Unit)? = null) =
    peelResponseOptions(response, isHideLoading = false, successBlock = successBlock)

/**
 * Check the [KsResponse]'s changing and do the corresponding reaction without showing the loading view.
 */
fun <D> LoadView.peelResponseSkipLoading(response: KsResponse<D>, successBlock: ((D) -> Unit)? = null) =
    peelResponseOptions(response, false, successBlock = successBlock)

/**
 * Check the [KsResponse]'s changing and do the corresponding reaction. Here're three data
 * type [Loading], [Success], and [Error].
 *
 * - [Loading] state will show the loading view.
 * - [Success] state will extract the data from the inside class to be used [successBlock].
 * - [Error] state will show the error view and msg to the user. Also, you can use [errorBlock] manually.
 */
private fun <D> LoadView.peelResponseOptions(
    response: KsResponse<D>,
    isShowLoading: Boolean = true,
    isShowError: Boolean = true,
    isHideLoading: Boolean = true,
    errorBlock: ((String) -> Unit)? = null,
    successBlock: ((D) -> Unit)? = null
) = response.also {
    when (it) {
        is Loading<*> -> if (isShowLoading) showLoading()
        is Success<D> -> {
            it.data?.let { successBlock?.invoke(it) }
            if (isShowLoading && isHideLoading) hideLoading()
        }
        is Error<*> -> {
            if (isShowLoading) hideLoading()
            if (isShowError) showError(it.msg)
            errorBlock?.invoke(it.msg)
        }
    }
}

/**
 * Abort the await request.
 */
inline fun <T> Deferred<T>?.abort(cause: Throwable? = null) =
    takeIf { null != it && it.isActive }?.cancel(cause)
