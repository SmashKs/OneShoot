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

import androidx.recyclerview.widget.RecyclerView
import com.devrapid.adaptiverecyclerview.AdaptiveAdapter
import com.devrapid.adaptiverecyclerview.AdaptiveViewHolder
import com.devrapid.adaptiverecyclerview.IVisitable

typealias KsViewHolder = AdaptiveViewHolder<MultiTypeFactory, KsMultiVisitable>
typealias KsMultiVisitable = IVisitable<MultiTypeFactory>
typealias KsAdapter = AdaptiveAdapter<MultiTypeFactory, KsMultiVisitable, KsViewHolder>
typealias MultiData = MutableList<KsMultiVisitable>

typealias RVAdapterAny = RecyclerView.Adapter<*>
