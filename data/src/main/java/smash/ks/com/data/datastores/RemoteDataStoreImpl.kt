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

import smash.ks.com.data.remote.services.KsFirebase
import smash.ks.com.data.remote.services.KsService
import smash.ks.com.data.repositories.DataRepositoryImpl.Companion.SWITCH
import smash.ks.com.domain.exceptions.NoParameterException
import smash.ks.com.domain.parameters.KsAnalyzeImageParam.Companion.PARAM_BYTE_ARRAY
import smash.ks.com.domain.parameters.KsParam.Companion.PARAM_NAME
import smash.ks.com.domain.parameters.Parameterable

/**
 * The implementation of the remote data store. The responsibility is selecting a correct
 * remote service to access the data.
 */
class RemoteDataStoreImpl(
    private val ksService: KsService,
    private val ksFirebase: KsFirebase
) : DataStore {
    override fun getKsImage(params: Parameterable?) = params?.toParameter()?.let {
        if (SWITCH)
            ksService.retrieveKsData(it)
        else {
            val name = it[PARAM_NAME] ?: throw NullPointerException()

            ksFirebase.retrieveImages(name)
        }
    } ?: throw NoParameterException()

    override fun keepKsImage(params: Parameterable) = throw UnsupportedOperationException()

    override fun pushImageToCloud(params: Parameterable) = ksFirebase.uploadImage(params)

    override fun analyzeImageTagsByML(params: Parameterable) = params.toAnyParameter().let {
        val byteArray = it[PARAM_BYTE_ARRAY] ?: throw NullPointerException()
        byteArray as? ByteArray ?: throw ClassCastException()

        ksFirebase.retrieveImageTagsByML(byteArray)
    }

    override fun analyzeImageWordContentByML(params: Parameterable) = ksFirebase.retrieveImageWordContentByML(params)
}
