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

package smash.ks.com.oneshoot.external.sqlite.v1

import com.raizlabs.android.dbflow.kotlinextensions.eq
import com.raizlabs.android.dbflow.kotlinextensions.from
import com.raizlabs.android.dbflow.kotlinextensions.select
import com.raizlabs.android.dbflow.kotlinextensions.where
import com.raizlabs.android.dbflow.rx2.kotlinextensions.list
import com.raizlabs.android.dbflow.rx2.kotlinextensions.rx
import smash.ks.com.data.local.services.KsDatabase
import smash.ks.com.data.objects.KsModel
import smash.ks.com.domain.parameters.Parameterable
import smash.ks.com.oneshoot.entities.KsEntity
import smash.ks.com.oneshoot.entities.KsEntity_Table

class KsDbFlowImpl : KsDatabase {
    override fun fetchKsData(params: Parameterable) =
        (select from KsEntity::class where (KsEntity_Table.id eq 4)).rx().list.map {
            val (id, uri) = it.first()

            KsModel(id, uri)
        }
}