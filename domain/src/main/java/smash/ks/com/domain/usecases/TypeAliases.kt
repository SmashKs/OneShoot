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

package smash.ks.com.domain.usecases

import smash.ks.com.domain.CompletableUseCase
import smash.ks.com.domain.SingleUseCase
import smash.ks.com.domain.objects.KsObject
import smash.ks.com.domain.usecases.analysis.GetImageContentWordsUsecase
import smash.ks.com.domain.usecases.analysis.GetImageTagsUsecase
import smash.ks.com.domain.usecases.fake.GetKsImageUsecase
import smash.ks.com.domain.usecases.upload.UploadImageToFirebaseUsecase

//region Fake
typealias GetKsImageCase = SingleUseCase<KsObject, GetKsImageUsecase.Requests>
//endregion

typealias GetImageTagsCase = SingleUseCase<List<String>, GetImageTagsUsecase.Requests>

typealias GetImageContentWordsCase = SingleUseCase<String, GetImageContentWordsUsecase.Requests>

typealias UploadImageToFirebaseCase = CompletableUseCase<UploadImageToFirebaseUsecase.Requests>
