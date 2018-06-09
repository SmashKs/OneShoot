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

package smash.ks.com.oneshoot.widgets.recyclerview

import android.content.Context
import android.view.ViewGroup
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import smash.ks.com.ext.const.DEFAULT_INT

/**
 * The common recyclerview adapter for the multi-type object. Avoid that we create
 * a lots similar boilerplate adapters.
 */
open class MultiTypeAdapter(
    override var dataList: MultiData,
    context: Context
) : KsAdapter(), KodeinAware {
    override var typeFactory: MultiTypeFactory
        get() = multiTypeFactory
        set(value) = throw UnsupportedOperationException("We don't allow this method to use!")
    override val kodein by closestKodein(context)
    protected var viewType = DEFAULT_INT
    private val multiTypeFactory by instance<MultiTypeFactory>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KsViewHolder {
        this.viewType = viewType

        return super.onCreateViewHolder(parent, viewType)
    }
}
