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

fun <T, Y : Any> MutableLiveData<KsResponse>.askingData(
    block: suspend CoroutineScope.() -> Deferred<T>,
    successBlock: suspend CoroutineScope.(res: Deferred<T>) -> Y
) {
    var entity: Deferred<T>? = null

    ui {
        value = KsResponse.Loading<Any>()

        try {
            entity = block()
        }
        catch (e: Exception) {
            value = KsResponse.Error(null, e.message.orEmpty())
        }

        if (null != entity) {
            // This block already checked the entity isn't a null variable.
            value = KsResponse.Success(successBlock(entity!!))
        }
    }
}
