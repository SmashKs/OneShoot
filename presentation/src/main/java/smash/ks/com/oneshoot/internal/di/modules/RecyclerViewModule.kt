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
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearLayoutManager.HORIZONTAL
import android.support.v7.widget.LinearLayoutManager.VERTICAL
import org.kodein.di.Kodein.Module
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.setBinding
import smash.ks.com.oneshoot.internal.di.tag.ObjectLabel.LINEAR_LAYOUT_HORIZONTAL
import smash.ks.com.oneshoot.internal.di.tag.ObjectLabel.LINEAR_LAYOUT_VERTICAL
import smash.ks.com.oneshoot.widgets.recyclerview.MultiTypeFactory

/**
 * To provide the necessary objects into the recycler view.
 */
object RecyclerViewModule {
    fun recyclerViewProvider(context: Context) = Module {
        // Linear Layout Manager.
        bind<LinearLayoutManager>(LINEAR_LAYOUT_VERTICAL) with provider {
            LinearLayoutManager(context, VERTICAL, false)
        }
        bind<LinearLayoutManager>(LINEAR_LAYOUT_HORIZONTAL) with provider {
            LinearLayoutManager(context, HORIZONTAL, false)
        }

        bind<MultiTypeFactory>() with instance(MultiTypeFactory(context))

        /** ViewModel Set for [MultiTypeFactory] */
        bind() from setBinding<ViewHolderEntry>()
    }
}
