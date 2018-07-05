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
import smash.ks.com.data.datas.DataLabelMapper
import smash.ks.com.data.datas.LabelData
import smash.ks.com.domain.models.LabelModel

/**
 * A transforming mapping between [LabelData] and [LabelModel]. The different layers have
 * their own data objects, the objects should transform and fit each layers.
 */
class LabelMapper constructor(mapper: ModelMapper) : DataLabelMapper(mapper) {
    override fun toModelFrom(data: LabelData): LabelModel = mapper.map(data, LabelModel::class.java)

    override fun toDataFrom(model: LabelModel): LabelData = mapper.map(model, LabelData::class.java)
}
