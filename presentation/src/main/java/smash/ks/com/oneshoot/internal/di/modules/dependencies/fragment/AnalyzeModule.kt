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
import smash.ks.com.oneshoot.R
import smash.ks.com.oneshoot.entities.LabelEntity
import smash.ks.com.oneshoot.features.photograph.AnalyzeViewModel
import smash.ks.com.oneshoot.features.photograph.LabelsViewHolder
import smash.ks.com.oneshoot.features.photograph.UploadPicViewModel
import smash.ks.com.oneshoot.internal.di.modules.ViewHolderEntry
import smash.ks.com.oneshoot.internal.di.modules.ViewModelEntry
import smash.ks.com.oneshoot.internal.di.scope.fragmentScope
import smash.ks.com.oneshoot.internal.di.tag.ObjectLabel.LABEL_ADAPTER
import smash.ks.com.oneshoot.widgets.recyclerview.MultiTypeAdapter
import smash.ks.com.oneshoot.widgets.recyclerview.RVAdapterAny

object AnalyzeModule {
    fun analyzeProvider() = Module("Analyze A Picture Fragment Module") {
        // *** ViewModel
        bind<ViewModelEntry>().inSet() with provider {
            AnalyzeViewModel::class.java to AnalyzeViewModel(instance(), instance())
        }
        bind<ViewModelEntry>().inSet() with provider {
            UploadPicViewModel::class.java to UploadPicViewModel(instance())
        }

        // *** ViewHolder
        bind<ViewHolderEntry>().inSet() with provider {
            LabelEntity::class.hashCode() to Pair(R.layout.item_label, ::LabelsViewHolder)
        }

        // *** Others
        bind<RVAdapterAny>(LABEL_ADAPTER) with scoped(fragmentScope).singleton {
            MultiTypeAdapter(mutableListOf(), context)
        }
    }
}
