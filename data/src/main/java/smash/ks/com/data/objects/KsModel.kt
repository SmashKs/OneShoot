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

package smash.ks.com.data.objects

import com.ks.smash.ext.const.DEFAULT_LONG
import com.ks.smash.ext.const.DEFAULT_STR
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import smash.ks.com.data.local.v1.dbflow.KsDatabaseConfig
import smash.ks.com.domain.parameters.Parameterable

@Table(database = KsDatabaseConfig::class, allFields = true, name = "KsEntityTable")
data class KsModel(
    @PrimaryKey(autoincrement = true)
    var id: Long = DEFAULT_LONG,
    var uri: String = DEFAULT_STR
) : Model, Parameterable {
    override fun toParameter() = hashMapOf(
        "id" to id.toString(),
        "uri" to uri)
}