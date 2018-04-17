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
import com.devrapid.adaptiverecyclerview.ViewTypeFactory
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import smash.ks.com.oneshoot.internal.di.modules.ViewHolderEntries

class MultiTypeFactory(context: Context) : ViewTypeFactory(), KodeinAware {
    override var transformMap
        get() = viewHolderEntries.toMap().toMutableMap()
        set(value) = Unit
    override val kodein by closestKodein(context)
    private val viewHolderEntries by instance<ViewHolderEntries>()

    // *** Here are the entity binding the specific hashcode. ***
    fun type(entity: MultiVisitable) = entity.javaClass.hashCode()
}