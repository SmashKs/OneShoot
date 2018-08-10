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

package smash.ks.com.domain.usecases.upload

import com.devrapid.kotlinshaver.castOrNull
import smash.ks.com.domain.ResponseUploadResult
import smash.ks.com.domain.SingleUseCase
import smash.ks.com.domain.executors.PostExecutionThread
import smash.ks.com.domain.executors.ThreadExecutor
import smash.ks.com.domain.models.response.KsResponse.Error
import smash.ks.com.domain.models.response.KsResponse.Success
import smash.ks.com.domain.parameters.KsPhotoToCloudinaryParam
import smash.ks.com.domain.repositories.DataRepository
import smash.ks.com.domain.usecases.upload.UploadImageToCloudinaryUsecase.Requests

class UploadImageToCloudinaryUsecase(
    private val repository: DataRepository,
    threadExecutor: ThreadExecutor,
    postExecutionThread: PostExecutionThread
) : SingleUseCase<ResponseUploadResult, Requests>(threadExecutor, postExecutionThread) {
    override fun fetchUseCase() = requireNotNull(requestValues?.run {
        repository
            .storeImageToCloudinary(params)
            .map {
                castOrNull<ResponseUploadResult>(Success(it)) ?: Error(msg = ClassCastException().message.orEmpty())
            }
    })

    /** Wrapping data requests for general situation.*/
    class Requests(val params: KsPhotoToCloudinaryParam = KsPhotoToCloudinaryParam()) : RequestValues
}
