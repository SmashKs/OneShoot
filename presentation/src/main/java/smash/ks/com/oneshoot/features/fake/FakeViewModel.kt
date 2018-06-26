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

import androidx.lifecycle.ViewModel
import smash.ks.com.domain.parameters.KsParam
import smash.ks.com.domain.usecases.GetKsImageCase
import smash.ks.com.domain.usecases.SaveKsImageCase
import smash.ks.com.oneshoot.entities.KsEntity
import smash.ks.com.oneshoot.entities.mappers.PresentationFakeMapper
import smash.ks.com.oneshoot.ext.presentation.ResponseLiveData
import smash.ks.com.oneshoot.ext.presentation.requestData
import smash.ks.com.oneshoot.ext.presentation.requestWithoutResponse
import smash.ks.com.oneshoot.ext.usecase.toAwait
import smash.ks.com.oneshoot.features.UntilPresenterLiveData
import smash.ks.com.domain.usecases.fake.FindKsImageUsecase.Requests as FetchImageRequest
import smash.ks.com.domain.usecases.fake.PersistKsImageUsecase.Requests as SaveImageRequest

class FakeViewModel(
    private val getKsImageCase: GetKsImageCase,
    private val saveKsImageCase: SaveKsImageCase,
    private val mapper: PresentationFakeMapper
) : ViewModel() {
    private val temp by lazy { ResponseLiveData<String>() }
    private val saveRes by lazy { UntilPresenterLiveData() }

    fun retrieveId(imageId: Int) =
        temp.requestData({
                             getKsImageCase.toAwait(mapper,
                                                    FetchImageRequest(KsParam(imageId.toLong(), "annehathaway")))
                         },
                         KsEntity::uri)

    fun storeImage() = saveRes.requestWithoutResponse {
        saveKsImageCase.toAwait(SaveImageRequest(KsParam(imageUri = "!??!?!?!?!?!??!")))
    }
}
