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

package smash.ks.com.oneshoot.features.photograph

import androidx.lifecycle.ViewModel
import smash.ks.com.domain.Labels
import smash.ks.com.domain.parameters.KsAnalyzeImageParam
import smash.ks.com.domain.usecases.GetImageTagsCase
import smash.ks.com.domain.usecases.analysis.FindImageTagsUsecase.Requests
import smash.ks.com.oneshoot.ext.presentation.ResponseLiveData
import smash.ks.com.oneshoot.ext.presentation.requestData
import smash.ks.com.oneshoot.ext.usecase.toAwait

class TakeAPicViewModel(
    private val getImageTagsCase: GetImageTagsCase
) : ViewModel() {
    val labels by lazy { ResponseLiveData<Labels>() }

    fun analyzeImage(byteArray: ByteArray) {
        labels.requestData { getImageTagsCase.toAwait(Requests(KsAnalyzeImageParam(byteArray))) }
    }
}

//private fun <T, R> SingleUseCase<T, R>.toAwait(requests: R): Deferred<KsResponse<T>> {
//
//}
