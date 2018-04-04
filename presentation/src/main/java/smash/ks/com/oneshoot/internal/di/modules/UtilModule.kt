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

package smash.ks.com.oneshoot.internal.di.modules

import android.content.Context
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import org.modelmapper.ModelMapper
import smash.ks.com.data.objects.KsModel
import smash.ks.com.data.objects.mappers.KsMapper
import smash.ks.com.domain.objects.KsObject
import smash.ks.com.oneshoot.entities.KsEntity
import smash.ks.com.oneshoot.entities.mappers.KsEntityMapper

object UtilModule {
    fun utilModule(context: Context) = Kodein.Module {
        bind<ModelMapper>() with instance(ModelMapper())
        bind<Gson>() with singleton {
            with(GsonBuilder()) {
                setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                setLenient()
                create()
            }
        }

        bind<smash.ks.com.data.objects.mappers.Mapper<KsModel, KsObject>>() with singleton {
            smash.ks.com.data.objects.mappers.KsMapper(instance())
        }
        bind<KsMapper>() with singleton { KsMapper(instance()) }
        bind<smash.ks.com.oneshoot.entities.mappers.Mapper<KsObject, KsEntity>>() with singleton {
            KsEntityMapper(instance())
        }
    }
}