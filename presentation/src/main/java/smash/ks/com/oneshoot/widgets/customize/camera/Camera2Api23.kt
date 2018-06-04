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

package smash.ks.com.oneshoot.widgets.customize.camera

import android.annotation.TargetApi
import android.content.Context
import android.graphics.ImageFormat.JPEG
import android.hardware.camera2.params.StreamConfigurationMap
import android.os.Build.VERSION_CODES.M
import smash.ks.com.oneshoot.widgets.customize.camera.module.CameraViewModule
import smash.ks.com.oneshoot.widgets.customize.camera.module.Preview
import smash.ks.com.oneshoot.widgets.customize.camera.module.Size
import smash.ks.com.oneshoot.widgets.customize.camera.module.SizeMap

/**
 * The Camera2 Api above Android api [M] 23.
 */
@TargetApi(M)
class Camera2Api23(
    callback: CameraViewModule.Callback?,
    preview: Preview,
    context: Context
) : Camera2(callback, preview, context) {
    override fun collectPictureSizes(sizes: SizeMap, map: StreamConfigurationMap) {
        // Try to get hi-res output sizes.
        map.getHighResolutionOutputSizes(JPEG)?.forEach { sizes.add(Size(it.width, it.height)) }

        if (sizes.isEmpty()) super.collectPictureSizes(sizes, map)
    }
}
