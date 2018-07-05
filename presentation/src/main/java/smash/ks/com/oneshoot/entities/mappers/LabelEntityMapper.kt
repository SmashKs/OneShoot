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

package smash.ks.com.oneshoot.entities.mappers

import org.modelmapper.ModelMapper
import smash.ks.com.domain.models.LabelModel
import smash.ks.com.oneshoot.entities.LabelEntity

/**
 * A transforming mapping between [LabelModel] and [LabelEntity]. The different layers have
 * their own data objects, the objects should transform and fit each layers.
 */
class LabelEntityMapper(mapper: ModelMapper) : PresentationLabelMapper(mapper) {
    override fun toEntityFrom(model: LabelModel): LabelEntity = mapper.map(model, LabelEntity::class.java)

    override fun toModelFrom(entity: LabelEntity): LabelModel = mapper.map(entity, LabelModel::class.java)
}
