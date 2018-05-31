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

package smash.ks.com.oneshoot.widgets.customize.camera.view

import android.content.Context
import android.util.SparseIntArray
import android.view.Display
import android.view.OrientationEventListener
import android.view.Surface.ROTATION_0
import android.view.Surface.ROTATION_180
import android.view.Surface.ROTATION_270
import android.view.Surface.ROTATION_90

/**
 * Monitors the value returned from [Display.getRotation].
 */
abstract class DisplayOrientationDetector(context: Context) {
    companion object {
        private const val DEGREE_0 = 0
        private const val DEGREE_90 = 0
        private const val DEGREE_180 = 0
        private const val DEGREE_270 = 0
        /** Mapping from Surface.Rotation_n to degrees.  */
        val DISPLAY_ORIENTATIONS = SparseIntArray()

        init {
            DISPLAY_ORIENTATIONS.put(ROTATION_0, DEGREE_0)
            DISPLAY_ORIENTATIONS.put(ROTATION_90, DEGREE_90)
            DISPLAY_ORIENTATIONS.put(ROTATION_180, DEGREE_180)
            DISPLAY_ORIENTATIONS.put(ROTATION_270, DEGREE_270)
        }
    }

    var display: Display? = null
    var lastKnownDisplayOrientation = 0
        private set
    private val orientationEventListener by lazy {
        object : OrientationEventListener(context) {
            /** This is either Surface.Rotation_0, _90, _180, _270, or -1 (invalid).  */
            private var lastKnownRotation = -1

            override fun onOrientationChanged(orientation: Int) {
                if (null == display || ORIENTATION_UNKNOWN == orientation) return

                val rotation = display!!.rotation

                if (lastKnownRotation != rotation) {
                    lastKnownRotation = rotation
                    dispatchOnDisplayOrientationChanged(DISPLAY_ORIENTATIONS.get(rotation))
                }
            }
        }
    }

    /**
     * Called when display orientation is changed.
     *
     * @param displayOrientation One of 0, 90, 180, and 270.
     */
    abstract fun onDisplayOrientationChanged(displayOrientation: Int)

    fun enable(display: Display) {
        this.display = display
        orientationEventListener.enable()
        // Immediately dispatch the first callback.
        dispatchOnDisplayOrientationChanged(DISPLAY_ORIENTATIONS.get(display.rotation))
    }

    fun disable() {
        orientationEventListener.disable()
        display = null
    }

    fun dispatchOnDisplayOrientationChanged(displayOrientation: Int) {
        lastKnownDisplayOrientation = displayOrientation
        onDisplayOrientationChanged(displayOrientation)
    }
}
