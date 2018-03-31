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

@file:Suppress("NOTHING_TO_INLINE")

package smash.ks.com.oneshoot.ext.stubview

import android.app.Activity
import android.support.annotation.IdRes
import android.view.View
import android.view.ViewStub
import com.devrapid.kotlinknifer.gone
import com.devrapid.kotlinknifer.visiable
import com.devrapid.kotlinshaver.isNotNull
import org.jetbrains.anko.find
import smash.ks.com.oneshoot.R

fun Activity.showViewStub(@IdRes stub: Int, @IdRes realView: Int) =
    find<ViewStub>(stub)
        .takeIf(Any::isNotNull)
        ?.visiable() ?: let { find<View>(realView).visiable() }

inline fun Activity.showLoadingView() = showViewStub(R.id.vs_loading, R.id.v_loading)
inline fun Activity.showErrorView() = showViewStub(R.id.vs_error, R.id.v_error)
inline fun Activity.showRetryView() = showViewStub(R.id.vs_retry, R.id.v_retry)

inline fun Activity.hideLoadingView() = find<View>(R.id.v_loading).gone()
inline fun Activity.hideErrorView() = find<View>(R.id.v_error).gone()
inline fun Activity.hideRetryView() = find<View>(R.id.v_retry).gone()
