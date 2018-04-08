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
import smash.ks.com.domain.objects.KsObject
import smash.ks.com.domain.objects.KsResponse
import smash.ks.com.domain.parameters.KsParam
import smash.ks.com.domain.usecases.GetKsImageCase
import smash.ks.com.domain.usecases.fake.GetKsImageUsecase
import smash.ks.com.oneshoot.entities.KsEntity
import smash.ks.com.oneshoot.entities.mappers.Mapper
import smash.ks.com.oneshoot.ext.usecase.awaitCase

class MainViewModel(
    private val getKsImageCase: GetKsImageCase,
    private val mapper: Mapper<KsObject, KsEntity>
) : ViewModel() {
    val temp by lazy { MutableLiveData<KsResponse>() }

    fun loading(imageId: Int) {
        ui {
            temp.value = KsResponse.Loading<Any>()

            val entity = getKsImageCase.awaitCase(mapper, GetKsImageUsecase.Requests(KsParam(imageId)))

            temp.value = KsResponse.Success(entity.await().uri)
        }
    }
}