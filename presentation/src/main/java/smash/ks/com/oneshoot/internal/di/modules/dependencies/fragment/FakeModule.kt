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

package smash.ks.com.oneshoot.internal.di.modules.dependencies.fragment

import org.kodein.di.Kodein.Module
import org.kodein.di.generic.bind
import org.kodein.di.generic.inSet
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.scoped
import org.kodein.di.generic.singleton
import smash.ks.com.ext.cast
import smash.ks.com.oneshoot.R
import smash.ks.com.oneshoot.entities.KsEntity
import smash.ks.com.oneshoot.features.fake.FakeViewHolder
import smash.ks.com.oneshoot.features.fake.FakeViewModel
import smash.ks.com.oneshoot.internal.di.modules.ViewHolderEntry
import smash.ks.com.oneshoot.internal.di.modules.ViewModelEntry
import smash.ks.com.oneshoot.internal.di.scope.fragmentScope
import smash.ks.com.oneshoot.internal.di.tag.ObjectLabel.KS_ADAPTER
import smash.ks.com.oneshoot.internal.di.tag.ObjectLabel.KS_ENTITY
import smash.ks.com.oneshoot.widgets.recyclerview.MultiData
import smash.ks.com.oneshoot.widgets.recyclerview.MultiTypeAdapter
import smash.ks.com.oneshoot.widgets.recyclerview.RVAdapterAny

object FakeModule {
    fun fakeProvider() = Module("module name") {
        // *** ViewModel
        bind<ViewModelEntry>().inSet() with provider {
            FakeViewModel::class.java to FakeViewModel(instance(), instance(), instance())
        }
        // *** ViewHolder
        bind<ViewHolderEntry>().inSet() with provider {
            KsEntity::class.hashCode() to Pair(R.layout.item_fake, ::FakeViewHolder)
        }

        // *** Others
        bind<MultiData>(KS_ENTITY) with scoped(fragmentScope).singleton {
            cast<MultiData>(mutableListOf(KsEntity(), KsEntity(), KsEntity(), KsEntity()))
        }
        bind<RVAdapterAny>(KS_ADAPTER) with scoped(fragmentScope).singleton {
            MultiTypeAdapter(instance(KS_ENTITY), context)
        }
    }
}
