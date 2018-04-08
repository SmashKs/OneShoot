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

package smash.ks.com.oneshoot.ext.aac

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import smash.ks.com.domain.objects.KsResponse

fun <T> LifecycleOwner.observe(liveData: LiveData<T>, block: (T?) -> Unit) =
    liveData.observe(this, Observer(block))

fun responseReaction(response: KsResponse?, successBlock: (KsResponse.Success<*>) -> Unit) {
    response?.also {
        when (it) {
            is KsResponse.Loading<*> -> {
                /** show the loading view */
            }
            is KsResponse.Success<*> -> {
                successBlock(it)
                /** hide the loading view */
//                hideLoading()
            }
            is KsResponse.Error<*> -> {
                /** show the error view */
//                showError(it.msg)
            }
        }
    }
}