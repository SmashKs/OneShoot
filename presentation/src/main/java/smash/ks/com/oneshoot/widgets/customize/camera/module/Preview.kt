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

package smash.ks.com.oneshoot.widgets.customize.camera.module

import android.graphics.SurfaceTexture
import android.view.Surface
import android.view.SurfaceHolder
import android.view.View

/**
 * Encapsulates all the operations related to camera preview in a backward-compatible manner.
 */
abstract class Preview {
    var onSurfaceChanged: (() -> Unit)? = null
    var width = 0
        private set
    var height = 0
        private set
    open var surfaceHolder: SurfaceHolder? = null
    open var surfaceTexture: SurfaceTexture? = null

    abstract val surface: Surface
    abstract val view: View
    abstract val outputClass: Class<*>
    abstract val isReady: Boolean

    abstract fun setDisplayOrientation(displayOrientation: Int)

    protected fun dispatchSurfaceChanged() = onSurfaceChanged?.invoke() ?: Unit

    open fun setBufferSize(width: Int, height: Int) = Unit

    fun setSize(width: Int, height: Int) {
        this.width = width
        this.height = height
    }
}
