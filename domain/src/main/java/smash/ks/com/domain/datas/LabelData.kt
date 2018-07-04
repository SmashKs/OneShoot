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

package smash.ks.com.domain.datas

import smash.ks.com.ext.const.DEFAULT_LONG
import smash.ks.com.ext.const.DEFAULT_STR
import smash.ks.com.ext.const.UniqueId

/**
 * Data object in domain layer to be a bridge object.
 */
data class LabelData(
    var id: UniqueId = DEFAULT_LONG,
    var uri: String = DEFAULT_STR
) : Data
