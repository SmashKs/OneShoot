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

import smash.ks.com.data.local.services.KsDatabase
import smash.ks.com.data.local.services.KsFlow
import smash.ks.com.domain.exceptions.NoParameterException
import smash.ks.com.domain.parameters.KsAnalyzeImageParam.Companion.PARAM_BYTE_ARRAY
import smash.ks.com.domain.parameters.KsParam
import smash.ks.com.domain.parameters.Parameterable

/**
 * The implementation of the local data store. The responsibility is selecting a correct
 * local service(Database/Local file) to access the data.
 */
class LocalDataStoreImpl(
    private val database: KsDatabase,
    private val flow: KsFlow
) : DataStore {
    // NOTE(jieyi): 2018/05/17 delay is for the simulation of the real API communication.

    //region Fake
    override fun getKsImage(params: Parameterable?) = params?.toParameter()?.run {
        val id = get(KsParam.PARAM_ID)?.toLong()

        database.retrieveKsData(id)
    } ?: throw NoParameterException()

    override fun keepKsImage(params: Parameterable) = params.toParameter().run {
        val id = get(KsParam.PARAM_ID)
        val uri = get(KsParam.PARAM_URI)

        if (null == id || null == uri) throw NullPointerException()

        database.storeKsData(id.toLong(), uri)
    }
    //endregion

    override fun pushImageToFirebase(params: Parameterable) = throw UnsupportedOperationException()

    override fun pushImageToCloudinary(params: Parameterable) = throw UnsupportedOperationException()

    override fun analyzeImageTagsByML(params: Parameterable) = params.toAnyParameter().let {
        val byteArray = it[PARAM_BYTE_ARRAY] ?: throw NullPointerException()
        byteArray as? ByteArray ?: throw ClassCastException()

        flow.retrieveImageTagsByML(byteArray)
    }

    override fun analyzeImageWordContentByML(params: Parameterable) = flow.retrieveImageWordContentByML(params)
}
