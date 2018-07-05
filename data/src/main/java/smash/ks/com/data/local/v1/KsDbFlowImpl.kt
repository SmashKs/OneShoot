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

package smash.ks.com.data.local.v1

import com.devrapid.kotlinshaver.isNotNull
import com.devrapid.kotlinshaver.toSingle
import com.raizlabs.android.dbflow.kotlinextensions.eq
import com.raizlabs.android.dbflow.kotlinextensions.from
import com.raizlabs.android.dbflow.kotlinextensions.select
import com.raizlabs.android.dbflow.kotlinextensions.where
import com.raizlabs.android.dbflow.rx2.kotlinextensions.list
import com.raizlabs.android.dbflow.rx2.kotlinextensions.rx
import com.raizlabs.android.dbflow.sql.language.Delete
import smash.ks.com.data.datas.KsData
import smash.ks.com.data.datas.KsData_Table
import smash.ks.com.data.local.services.KsDatabase
import smash.ks.com.ext.const.UniqueId
import java.util.Random
import java.util.UUID

/**
 * The implementation for accessing the data by dbflow with [io.reactivex.plugins.RxJavaPlugins].
 */
class KsDbFlowImpl : KsDatabase {
    override fun retrieveKsData(id: UniqueId?) = (id?.let {
        (select from KsData::class where (KsData_Table.id eq it)).rx()
    } ?: let { (select from KsData::class).rx() })
        .list
        .map {
            val (uid, uri) = try {
                it.first()
            }
            catch (exception: NoSuchElementException) {
                KsData(Random().nextLong(), UUID.randomUUID().toString())
            }

            KsData(uid, uri)
        }

    override fun storeKsData(id: UniqueId, uri: String) = KsData(id, uri).save().ignoreElement()

    override fun deleteKsData(data: KsData?) =
        (data.takeIf(Any?::isNotNull)?.delete() ?: Delete.table(KsData::class.java).toSingle()).ignoreElement()
}
