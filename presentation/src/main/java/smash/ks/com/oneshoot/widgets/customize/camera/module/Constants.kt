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

internal object Constants {
    val DEFAULT_ASPECT_RATIO by lazy { AspectRatio.of(4, 3) }

    /** The camera device faces the opposite direction as the device's screen.  */
    const val FACING_BACK = 0
    /** The camera device faces the same direction as the device's screen.  */
    const val FACING_FRONT = 1

    /** Flash will not be fired.  */
    const val FLASH_OFF = 0
    /** Flash will always be fired during snapshot.  */
    const val FLASH_ON = 1
    /** Constant emission of light during preview, auto-focus and snapshot.  */
    const val FLASH_TORCH = 2
    /** Flash will be fired automatically when required.  */
    const val FLASH_AUTO = 3
    /** Flash will be fired in red-eye reduction mode.  */
    const val FLASH_RED_EYE = 4

    const val LANDSCAPE_90 = 90
    const val LANDSCAPE_270 = 270
}
