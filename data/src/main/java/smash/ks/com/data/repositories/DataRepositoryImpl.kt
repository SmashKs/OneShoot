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

package smash.ks.com.data.repositories

import com.ks.smash.ext.internal.di.qulifiers.Local
import com.ks.smash.ext.internal.di.qulifiers.Remote
import smash.ks.com.data.datastores.DataStore
import smash.ks.com.data.local.cache.KsCache
import smash.ks.com.data.objects.mappers.KsMapper
import smash.ks.com.domain.parameters.Parameterable
import smash.ks.com.domain.repositories.DataRepository
import javax.inject.Inject

class DataRepositoryImpl @Inject constructor(
    @Local private val cache: KsCache,
    @Local private val local: DataStore,
    @Remote private val remote: DataStore,
    private val mapper: KsMapper
) : DataRepository {
    //region Fake
    override fun retrieveKsImage(params: Parameterable) =
        (if (true) local else remote).fetchKsImage().map(mapper::toObjectFrom)
    //endregion
}