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

package smash.ks.com.data.datastores

import io.reactivex.Completable
import io.reactivex.Single
import smash.ks.com.data.datas.KsData
import smash.ks.com.data.datas.LabelDatas
import smash.ks.com.data.datas.UploadResultData
import smash.ks.com.domain.Label
import smash.ks.com.domain.parameters.KsAnalyzeImageParam
import smash.ks.com.domain.parameters.KsParam
import smash.ks.com.domain.parameters.KsPhotoToCloudinaryParam
import smash.ks.com.domain.parameters.Parameterable
import smash.ks.com.domain.parameters.PhotoParam

/**
 * This interface will be similar to [smash.ks.com.domain.repositories.DataRepository]
 */
interface DataStore {
    //region Fake
    /**
     * Fetching an image object [KsData] by parameter.
     *
     * @param params [KsParam]
     * @return Rx [Single] object to the presentation layer.
     */
    fun getKsImage(params: Parameterable? = null): Single<KsData>

    /**
     * Keep an image object [KsData] by parameter.
     *
     * @param params [KsParam]
     * @return Rx [Completable] and no response to the presentation layer.
     */
    fun keepKsImage(params: Parameterable = KsParam()): Completable
    //endregion

    /**
     * Upload an image object to Firebase service.
     *
     * @param params [PhotoParam]
     * @return Rx [Completable] and no response to the presentation layer.
     */
    fun pushImageToFirebase(params: Parameterable = PhotoParam()): Completable

    /**
     * Upload an image object to Cloudinary service.
     *
     * @param params [KsPhotoToCloudinaryParam]
     * @return Rx [Completable] and no response to the presentation layer.
     */
    fun pushImageToCloudinary(params: Parameterable = KsPhotoToCloudinaryParam()): Single<UploadResultData>

    /**
     * Send an image object to machine learning model and get the tags of an image automatically.
     *
     * @param params [KsAnalyzeImageParam]
     * @return Rx [Single] with tag list to the presentation layer.
     */
    fun analyzeImageTagsByML(params: Parameterable = KsAnalyzeImageParam()): Single<LabelDatas>

    /**
     * Send an image object to machine learning model and get the content of an image automatically.
     *
     * @param params
     * @return Rx [Single] with the image content to the presentation layer.
     */
    fun analyzeImageWordContentByML(params: Parameterable): Single<Label>
}
