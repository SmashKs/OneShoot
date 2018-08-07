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

import com.devrapid.kotlinshaver.cast
import io.reactivex.Single
import smash.ks.com.data.datas.DataMapper
import smash.ks.com.data.datas.MapperPool
import smash.ks.com.data.datas.mappers.KsMapper
import smash.ks.com.data.datas.mappers.LabelMapper
import smash.ks.com.data.datas.mappers.UploadResultMapper
import smash.ks.com.data.datastores.DataStore
import smash.ks.com.data.local.cache.KsCache
import smash.ks.com.domain.models.UploadResultModel
import smash.ks.com.domain.parameters.Parameterable
import smash.ks.com.domain.repositories.DataRepository

/**
 * The data repository for being responsible for selecting an appropriate data store to access
 * the data.
 *
 * @property cache cache data store.
 * @property local from database/file/memory data store.
 * @property remote from remote service/firebase/third-party service data store.
 * @property mapperPool keeping all of the data mapper here.
 */
class DataRepositoryImpl constructor(
    private val cache: KsCache,
    private val local: DataStore,
    private val remote: DataStore,
    private val mapperPool: MapperPool
) : DataRepository {
    companion object {
        const val SWITCH = false
    }

    private val ksMapper by lazy { digDataMapper<KsMapper>() }
    private val labelMapper by lazy { digDataMapper<LabelMapper>() }
    private val photoResultMapper by lazy { digDataMapper<UploadResultMapper>() }

    //region Fake
    override fun fetchKsImage(params: Parameterable?) =
        (if (SWITCH) local else remote).getKsImage(params).map(ksMapper::toModelFrom)

    override fun storeKsImage(params: Parameterable) = (if (SWITCH) local else remote).keepKsImage(params)
    //endregion

    override fun uploadImage(params: Parameterable) = remote.pushImageToFirebase(params)

    override fun storeImageToCloudinary(params: Parameterable): Single<UploadResultModel> {
        println("===============22222222222222222=================")
        return remote.pushImageToCloudinary(params).map(photoResultMapper::toModelFrom)
    }

    override fun fetchImageTagsByML(params: Parameterable) =
        (if (SWITCH) local else remote).analyzeImageTagsByML(params).map { it.map(labelMapper::toModelFrom) }

    override fun fetchImageWordContentByML(params: Parameterable) =
        (if (SWITCH) local else remote).analyzeImageWordContentByML(params)

    private inline fun <reified DM : DataMapper> digDataMapper() = cast<DM>(mapperPool[DM::class.java])
}
