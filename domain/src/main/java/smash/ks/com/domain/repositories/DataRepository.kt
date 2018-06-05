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

package smash.ks.com.domain.repositories

import io.reactivex.Completable
import io.reactivex.Single
import smash.ks.com.domain.datas.KsData
import smash.ks.com.domain.parameters.KsParam
import smash.ks.com.domain.parameters.Parameterable

/**
 * This interface will be the similar to [smash.ks.com.data.datastores.DataStore] .
 */
interface DataRepository {
    //region Fake
    fun retrieveKsImage(params: Parameterable?): Single<KsData>

    fun storeKsImage(params: Parameterable = KsParam()): Completable
    //endregion

    fun uploadImage(params: Parameterable): Completable

    fun retrieveImageTagsByML(params: Parameterable): Single<List<String>>

    fun retrieveImageWordContentByML(params: Parameterable): Single<String>
}
