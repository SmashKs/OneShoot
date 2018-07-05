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

package smash.ks.com.data.datas

import smash.ks.com.data.datas.mappers.Mapper
import smash.ks.com.domain.models.KsModel
import smash.ks.com.domain.models.LabelModel

typealias DataFakeMapper = Mapper<KsData, KsModel>
typealias DataLabelMapper = Mapper<LabelData, LabelModel>

//region Data

typealias ImageHashCode = String
typealias ImageUri = String
typealias Uris = Map<ImageHashCode, ImageUri>

typealias Tag = String
typealias TagUri = String
typealias Tags = Map<Tag, TagUri>

typealias KsLabels = List<LabelData>
//endregion
