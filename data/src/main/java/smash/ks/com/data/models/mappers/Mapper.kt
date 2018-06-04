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
import smash.ks.com.data.models.Model
import smash.ks.com.domain.datas.Data

/**
 * The mapper is used to transition the object between [Model] and [Data].
 */
abstract class Mapper<M : Model, D : Data>(protected val mapper: ModelMapper) {
    /**
     * Transition the [Model] object to [Data] object.
     *
     * @param model a [Model] data object.
     * @return the same content's [Data] object.
     */
    abstract fun toDataFrom(model: M): D

    /**
     * Transition the [Data] object to [Model] object.
     *
     * @param obj a [Data] data object.
     * @return the same content's [Model] object.
     */
    abstract fun toModelFrom(obj: D): M
}
