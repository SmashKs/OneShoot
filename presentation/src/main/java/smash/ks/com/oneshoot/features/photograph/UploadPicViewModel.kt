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
import smash.ks.com.domain.Tag
import smash.ks.com.domain.parameters.PhotoParam
import smash.ks.com.domain.usecases.UploadImageToFirebaseCase
import smash.ks.com.domain.usecases.upload.UploadImageToFirebaseUsecase.Requests
import smash.ks.com.ext.const.DEFAULT_STR
import smash.ks.com.oneshoot.ext.presentation.requestWithoutResponse
import smash.ks.com.oneshoot.ext.usecase.toAwait
import smash.ks.com.oneshoot.features.UntilPresenterLiveData

class UploadPicViewModel(
    private val uploadImageToFirebaseCase: UploadImageToFirebaseCase
) : ViewModel() {
    val uploadRes by lazy { UntilPresenterLiveData() }

    fun uploadPhoto(imageBytes: ByteArray, title: String, author: String, tags: List<Tag>) =
        uploadRes.requestWithoutResponse {
            uploadImageToFirebaseCase.toAwait(Requests(PhotoParam(DEFAULT_STR, imageBytes, title, author, tags)))
        }
}
