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

package smash.ks.com.oneshoot.features.photograph

import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.N
import android.text.Html
import android.text.Html.FROM_HTML_MODE_LEGACY
import android.view.View
import kotlinx.android.synthetic.main.item_label.view.tv_label
import smash.ks.com.oneshoot.R
import smash.ks.com.oneshoot.entities.LabelEntity
import smash.ks.com.oneshoot.widgets.viewmodel.LabelVH

class LabelsViewHolder(view: View) : LabelVH(view) {
    override fun initView(model: LabelEntity, position: Int, adapter: Any) {
        itemView.apply {
            val formatted = String.format(context.getString(R.string.label_format), model.label, model.confidence)
            tv_label.text =
                (if (SDK_INT >= N) Html.fromHtml(formatted, FROM_HTML_MODE_LEGACY) else Html.fromHtml(formatted))
        }
    }
}
