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
import smash.ks.com.domain.datas.Data
import smash.ks.com.oneshoot.entities.Entity

/**
 * The mapper is used to transition the object between [Data] and [Entity].
 */
abstract class Mapper<D : Data, E : Entity>(protected val mapper: ModelMapper) {
    /**
     * Transition the [Data] object to [Entity] object.
     *
     * @param data a [Data] data object.
     * @return the same content's [Entity] object.
     */
    abstract fun toEntityFrom(data: D): E

    /**
     * Transition the [Data] object to [Entity] object.
     *
     * @param entity a [Entity] data object.
     * @return the same content's [Data] object.
     */
    abstract fun toDataFrom(entity: E): D
}
