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
import smash.ks.com.oneshoot.bases.LoadView

inline fun <reified T> LifecycleOwner.observe(liveData: LiveData<T>, noinline block: (T?) -> Unit) =
    liveData.observe(this, Observer(block))

fun <D> LoadView.breakResponse(response: KsResponse<D>?, successBlock: (KsResponse.Success<D>) -> Unit) =
    response?.also {
        when (it) {
            is KsResponse.Loading<*> -> showLoading()
            is KsResponse.Success<D> -> {
                successBlock(it)
                hideLoading()
            }
            is KsResponse.Error<*> -> {
                hideLoading()
                showError(it.msg)
            }
        }
    }

inline fun <T> Deferred<T>?.abort(cause: Throwable? = null) =
    takeIf { null != it && it.isActive }?.cancel(cause)
