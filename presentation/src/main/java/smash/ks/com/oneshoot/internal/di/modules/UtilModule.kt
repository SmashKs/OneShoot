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
import com.google.gson.FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.kodein.di.Kodein.Module
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.setBinding
import org.kodein.di.generic.singleton
import org.modelmapper.ModelMapper
import smash.ks.com.data.models.DataFakeMapper
import smash.ks.com.data.models.mappers.KsMapper
import smash.ks.com.oneshoot.entities.mappers.KsEntityMapper
import smash.ks.com.oneshoot.entities.mappers.PresentationFakeMapper

/**
 * To provide the necessary utility objects for the whole app.
 */
object UtilModule {
    fun utilProvider(context: Context) = Module("Util Module") {
        /** ViewModel Set for [smash.ks.com.oneshoot.widgets.viewmodel.ViewModelFactory] */
        /** ViewModel Set for [smash.ks.com.oneshoot.widgets.viewmodel.ViewModelFactory] */
        bind() from setBinding<ViewModelEntry>()

        bind<ModelMapper>() with instance(ModelMapper())
        bind<Gson>() with singleton {
            with(GsonBuilder()) {
                setFieldNamingPolicy(LOWER_CASE_WITH_UNDERSCORES)
                setLenient()
                create()
            }
        }

        bind<DataFakeMapper>() with singleton { KsMapper(instance()) }
        bind<PresentationFakeMapper>() with singleton { KsEntityMapper(instance()) }
    }
}
