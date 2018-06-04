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

import android.arch.lifecycle.ViewModel
import smash.ks.com.domain.parameters.KsParam
import smash.ks.com.domain.usecases.GetKsImageCase
import smash.ks.com.domain.usecases.SaveKsImageCase
import smash.ks.com.oneshoot.entities.mappers.PresentationFakeMapper
import smash.ks.com.oneshoot.ext.presentation.askingData
import smash.ks.com.oneshoot.ext.presentation.noResponseRequest
import smash.ks.com.oneshoot.ext.usecase.awaitCase
import smash.ks.com.oneshoot.features.ResponseLiveData
import smash.ks.com.domain.usecases.fake.GetKsImageUsecase.Requests as FetchImageRequest
import smash.ks.com.domain.usecases.fake.SaveKsImageUsecase.Requests as SaveImageRequest

class FakeViewModel(
    private val getKsImageCase: GetKsImageCase,
    private val saveKsImageCase: SaveKsImageCase,
    private val mapper: PresentationFakeMapper
) : ViewModel() {
    val temp by lazy { ResponseLiveData() }
    val saveRes by lazy { ResponseLiveData() }

    fun retrieveId(imageId: Int) {
        temp.askingData({ getKsImageCase.awaitCase(mapper, FetchImageRequest(KsParam(imageId.toLong()))) }) { res ->
            res.await().uri
        }
    }

    fun storeImage() {
        // TODO(jieyi): 2018/06/03 If we don't return value to ui activity/fragment, we shouldn't use an variable.
        saveRes.noResponseRequest { saveKsImageCase.awaitCase(SaveImageRequest(KsParam(imageUri = "This is my name"))) }
    }
}
