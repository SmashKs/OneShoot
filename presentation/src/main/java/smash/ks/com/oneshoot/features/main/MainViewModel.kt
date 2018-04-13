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

package smash.ks.com.oneshoot.features.main

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.devrapid.kotlinknifer.ui
import kotlinx.coroutines.experimental.Deferred
import smash.ks.com.domain.objects.KsObject
import smash.ks.com.domain.objects.KsResponse
import smash.ks.com.domain.parameters.KsParam
import smash.ks.com.domain.usecases.GetKsImageCase
import smash.ks.com.domain.usecases.fake.GetKsImageUsecase
import smash.ks.com.oneshoot.entities.KsEntity
import smash.ks.com.oneshoot.entities.mappers.Mapper
import smash.ks.com.oneshoot.ext.aac.abort
import smash.ks.com.oneshoot.ext.usecase.awaitCase

class MainViewModel(
    private val getKsImageCase: GetKsImageCase,
    private val mapper: Mapper<KsObject, KsEntity>
) : ViewModel() {
    val temp by lazy { MutableLiveData<KsResponse>() }
    private lateinit var entity: Deferred<KsEntity>

    fun loading(imageId: Int) {
        // TODO(jieyi): 2018/04/13 Here might extract a good extension for updating.
        ui {
            temp.value = KsResponse.Loading<Any>()

            try {
                entity = getKsImageCase.awaitCase(mapper, GetKsImageUsecase.Requests(KsParam(imageId)))
            }
            catch (e: Exception) {
                temp.value = KsResponse.Error(null, e.message.orEmpty())
            }

            temp.value = KsResponse.Success(entity.await().uri)
        }

//        temp.normal({
//                        getKsImageCase.awaitCase(mapper, Requests(KsParam(imageId)))
//                    }, { res ->
//                        res.await()
//                    })
    }

    inline fun <T> MutableLiveData<KsResponse>.normal(
        crossinline block: () -> Deferred<T>,
        crossinline success: (res: Deferred<T>) -> T
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

            if (null != entity) value = KsResponse.Success(success(entity!!))
        }
    }

    override fun onCleared() {
        // TODO(jieyi): 2018/04/13 We might make a good method for releasing them. (By reflection?)
        if (::entity.isInitialized) entity.abort()

        super.onCleared()
    }
}