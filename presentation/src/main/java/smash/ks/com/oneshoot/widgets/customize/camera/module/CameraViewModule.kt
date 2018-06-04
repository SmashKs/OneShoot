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

/**
 * The abstract for camera view model. We don't implement the methods and variables here.
 */
abstract class CameraViewModule(
    protected val callback: CameraViewModule.Callback?,
    protected val preview: Preview
) {
    val view get() = preview.view
    abstract val isCameraOpened: Boolean
    abstract var facing: Int
    abstract val supportedAspectRatios: Set<AspectRatio>
    abstract val aspectRatio: AspectRatio?
    abstract var autoFocus: Boolean
    abstract var flash: Int

    /**
     * @return `true` if the implementation was able to start the camera session.
     */
    abstract fun start(): Boolean

    abstract fun stop()

    /**
     * @return `true` if the aspect ratio was changed.
     */
    abstract fun setAspectRatio(ratio: AspectRatio): Boolean

    abstract fun takePicture()

    abstract fun setDisplayOrientation(displayOrientation: Int)

    /**
     * The event callbacks will be triggered when the camera open, close, or take a pic.
     * Also, we can obtain the photo raw data after the user took a pic.
     */
    interface Callback {
        fun onCameraOpened()
        fun onCameraClosed()
        fun onPictureTaken(data: ByteArray)
    }
}
