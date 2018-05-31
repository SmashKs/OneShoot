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

package smash.ks.com.oneshoot.ext.image.glide

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.support.annotation.DrawableRes
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority.HIGH
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.engine.DiskCacheStrategy.RESOURCE
import com.bumptech.glide.request.RequestOptions
import com.ks.smash.ext.const.DEFAULT_INT
import com.ks.smash.ext.const.takeUnlessDefault
import smash.ks.com.oneshoot.ext.resource.gContext

fun ImageView.loadByString(str: String, context: Context = gContext(), options: RequestOptions = glideKsOptions()) =
    glideDefault(context, options) { load(str) }

fun ImageView.loadByBitmap(bitmap: Bitmap, context: Context = gContext(), options: RequestOptions = glideKsOptions()) =
    glideDefault(context, options) { load(bitmap) }

fun ImageView.loadByUri(uri: Uri, context: Context = gContext(), options: RequestOptions = glideKsOptions()) =
    glideDefault(context, options) { load(uri) }

fun ImageView.loadByDrawable(
    drawable: Drawable,
    context: Context = gContext(),
    options: RequestOptions = glideKsOptions()
) =
    glideDefault(context, options) { load(drawable) }

fun ImageView.loadByAny(any: Any, context: Context = gContext(), options: RequestOptions = glideKsOptions()) =
    glideDefault(context, options) { load(any) }

fun glideKsOptions(
    @DrawableRes phResource: Int = DEFAULT_INT,
    @DrawableRes erSource: Int = DEFAULT_INT
) =
    RequestOptions().apply {
        centerCrop()
            phResource.takeUnlessDefault(::placeholder)
            erSource.takeUnlessDefault(::error)
        priority(HIGH)
        diskCacheStrategy(RESOURCE)
    }

fun glideObtaineBitmapFrom(uri: Uri, context: Context = gContext(), options: RequestOptions = glideKsOptions()) =
    glide(context)
        .asBitmap()
        .apply(options)
        .load(uri)
        .submit()
        .get()

fun glideObtaineDrawableFrom(uri: Uri, context: Context = gContext(), options: RequestOptions = glideKsOptions()) =
    glide(context)
        .asDrawable()
        .apply(options)
        .load(uri)
        .submit()
        .get()

private fun ImageView.glideDefault(
    context: Context = gContext(),
    options: RequestOptions = glideKsOptions(),
    block: RequestBuilder<Bitmap>.() -> RequestBuilder<Bitmap>
) = glide(context)
    .asBitmap()
    .apply(options)
    .block()
    .into(this)

private fun glide(context: Context) = Glide.with(context)

