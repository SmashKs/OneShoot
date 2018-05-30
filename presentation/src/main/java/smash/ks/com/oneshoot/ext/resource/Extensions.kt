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

package smash.ks.com.oneshoot.ext.resource

import android.support.annotation.ArrayRes
import android.support.annotation.DimenRes
import android.support.annotation.StringRes
import org.jetbrains.anko.dimen
import smash.ks.com.oneshoot.App

fun gContext() = App.appContext.applicationContext

fun gDimens(@DimenRes id: Int) = gContext().dimen(id)

fun gStrings(@StringRes id: Int) = gContext().getString(id)

fun gText(@StringRes id: Int) = gContext().getText(id)

fun gStringArray(@ArrayRes id: Int) = gContext().resources.getStringArray(id)

fun gTypeArray(@ArrayRes id: Int) = gContext().resources.obtainTypedArray(id)

fun gResArray(@ArrayRes id: Int) =
    gStringArray(id).mapIndexed { index, _ -> index to gTypeArray(id) }.toMutableList()

fun gResArrays(@ArrayRes vararg ids: Int) =
    ids.map(::gResArray).reduce { acc, new -> acc.addAll(new);acc }

