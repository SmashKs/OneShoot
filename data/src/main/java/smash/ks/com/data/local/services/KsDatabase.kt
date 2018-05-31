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

package smash.ks.com.data.local.services

import com.ks.smash.ext.const.DEFAULT_LONG
import com.ks.smash.ext.const.DEFAULT_STR
import io.reactivex.Completable
import io.reactivex.Single
import smash.ks.com.data.models.KsModel
import smash.ks.com.domain.parameters.KsParam
import smash.ks.com.domain.parameters.Parameterable

interface KsDatabase {
    fun fetchKsData(params: Parameterable = KsParam()): Single<KsModel>

    fun keepKsData(id: Long = DEFAULT_LONG, uri: String = DEFAULT_STR): Completable

    /**
     * Delete a row of the data from the database.
     *
     * @param model a model belongs to []
     * @return Completable
     */
    fun removeKsData(model: KsModel? = null): Completable
}
