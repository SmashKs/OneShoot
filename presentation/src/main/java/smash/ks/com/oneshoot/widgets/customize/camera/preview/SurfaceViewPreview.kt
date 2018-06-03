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

package smash.ks.com.oneshoot.widgets.customize.camera.preview

import android.content.Context
import android.support.v4.view.ViewCompat
import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.view.ViewGroup
import smash.ks.com.oneshoot.R
import smash.ks.com.oneshoot.widgets.customize.camera.module.Preview

internal class SurfaceViewPreview(context: Context, parent: ViewGroup) : Preview() {
    val surfaceView by lazy {
        View.inflate(context,
                     R.layout.surface_view,
                     parent).findViewById(R.id.surface_view) as? SurfaceView ?: throw ClassCastException()
    }
    override var surfaceHolder: SurfaceHolder? = null
        get() = surfaceView.holder
    override val surface: Surface get() = surfaceHolder!!.surface
    override val view get() = surfaceView
    override val outputClass get() = SurfaceHolder::class.java
    override val isReady get() = 0 != width && 0 != height

    init {
        surfaceView.holder.apply {
            setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
            addCallback(object : SurfaceHolder.Callback {
                override fun surfaceCreated(h: SurfaceHolder) {}

                override fun surfaceChanged(h: SurfaceHolder, format: Int, width: Int, height: Int) {
                    setSize(width, height)
                    if (!ViewCompat.isInLayout(surfaceView)) dispatchSurfaceChanged()
                }

                override fun surfaceDestroyed(h: SurfaceHolder) {
                    setSize(0, 0)
                }
            })
        }
    }

    override fun setDisplayOrientation(displayOrientation: Int) {}
}
