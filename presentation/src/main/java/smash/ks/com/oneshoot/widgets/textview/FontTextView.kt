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

package smash.ks.com.oneshoot.widgets.textview

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.TextView
import smash.ks.com.oneshoot.R

/**
 * It's able to set a font from imported resource font.
 */
class FontTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : TextView(context, attrs, defStyleAttr) {
    init {
        context.obtainStyledAttributes(attrs, R.styleable.FontTextView, defStyleAttr, 0).also {
            it.getString(R.styleable.FontTextView_textFont).let {
                typeface = Typeface.createFromAsset(context.assets, "fonts/$it")
            }
        }.recycle()
    }
}
