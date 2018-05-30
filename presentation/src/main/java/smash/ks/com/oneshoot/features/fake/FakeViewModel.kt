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

package smash.ks.com.oneshoot.features.fake

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import smash.ks.com.domain.objects.KsObject
import smash.ks.com.domain.objects.KsResponse
import smash.ks.com.domain.parameters.KsParam
import smash.ks.com.domain.usecases.GetKsImageCase
import smash.ks.com.domain.usecases.SaveKsImageCase
import smash.ks.com.domain.usecases.fake.GetKsImageUsecase.Requests
import smash.ks.com.domain.usecases.fake.SaveKsImageUsecase
import smash.ks.com.oneshoot.entities.KsEntity
import smash.ks.com.oneshoot.entities.mappers.Mapper
import smash.ks.com.oneshoot.ext.presentation.askingData
import smash.ks.com.oneshoot.ext.usecase.awaitCase

class FakeViewModel(
    private val getKsImageCase: GetKsImageCase,
    private val saveKsImageCase: SaveKsImageCase,
    private val mapper: Mapper<KsObject, KsEntity>
) : ViewModel() {
    val temp by lazy { MutableLiveData<KsResponse>() }
    val saveRes by lazy { MutableLiveData<KsResponse>() }

    fun retrieveId(imageId: Int) {
        temp.askingData({ getKsImageCase.awaitCase(mapper, Requests(KsParam(imageId))) }) { res ->
            res.await().uri
        }
    }

    fun storeImage() {
        saveRes.askingData({ saveKsImageCase.awaitCase(SaveKsImageUsecase.Requests(KsParam())) }) { res ->
            res.await()
        }
    }
}
