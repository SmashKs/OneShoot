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

package smash.ks.com.data.datas.mappers

import org.modelmapper.ModelMapper
import smash.ks.com.data.datas.DataUploadResultMapper
import smash.ks.com.data.datas.UploadResultData
import smash.ks.com.domain.models.UploadResultModel

/**
 * A transforming mapping between [UploadResultData] and [UploadResultModel]. The different layers have
 * their own data objects, the objects should transform and fit each layers.
 */
class UploadResultMapper constructor(mapper: ModelMapper) : DataUploadResultMapper(mapper) {
    // TODO(jieyi): 2018/08/07 Using [ModelMapper] to translate the [List].
    override fun toModelFrom(data: UploadResultData) = data.run {
        UploadResultModel(format, resourceType, secureUrl, createdAt, publicId, width, height, placeholder, tags)
    }

    override fun toDataFrom(model: UploadResultModel) = model.run {
        UploadResultData(format, resourceType, secureUrl, createdAt, publicId, width, height, placeholder, tags)
    }
}
