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
import smash.ks.com.domain.parameters.KsParam
import smash.ks.com.domain.parameters.Parameterable

class LocalDataStoreImpl(
    private val database: KsDatabase
) : DataStore {
    // NOTE(jieyi): 2018/05/17 delay is for the simulation of the real API communication.

    //region Fake
    override fun fetchKsImage(params: Parameterable) = database.fetchKsData(params)

    override fun keepKsImage(params: Parameterable) = params.toParameter().run {
        database.keepKsData(get(KsParam.PARAM_ID)!!.toLong(), get(KsParam.PARAM_URI)!!)
    }
    //endregion

    override fun uploadImage(params: Parameterable) = throw UnsupportedOperationException()

    override fun analyzeImageTagsByML(params: Parameterable) = throw UnsupportedOperationException()

    override fun analyzeImageWordContentByML(params: Parameterable) = throw UnsupportedOperationException()
}
