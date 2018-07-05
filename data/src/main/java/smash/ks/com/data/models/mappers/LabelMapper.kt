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

package smash.ks.com.data.models.mappers

import org.modelmapper.ModelMapper
import smash.ks.com.data.models.DataLabelMapper
import smash.ks.com.data.models.LabelModel
import smash.ks.com.domain.datas.LabelData

/**
 * A transforming mapping between [LabelModel] and [LabelData]. The different layers have
 * their own data objects, the objects should transform and fit each layers.
 */
class LabelMapper constructor(mapper: ModelMapper) : DataLabelMapper(mapper) {
    override fun toDataFrom(model: LabelModel): LabelData = mapper.map(model, LabelData::class.java)

    override fun toModelFrom(obj: LabelData): LabelModel = mapper.map(obj, LabelModel::class.java)
}
