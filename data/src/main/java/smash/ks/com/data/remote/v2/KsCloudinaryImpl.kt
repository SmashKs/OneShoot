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

package smash.ks.com.data.remote.v2

import android.net.Uri
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.devrapid.kotlinshaver.completable
import io.reactivex.CompletableEmitter
import smash.ks.com.data.remote.services.KsCloudinary

class KsCloudinaryImpl constructor(
    private val mediaManager: MediaManager
) : KsCloudinary {
    override fun hangImage(uri: Uri) = completable { mediaManager.upload(uri).callback(uploadCallback(it)) }

    override fun hangImage(byteArray: ByteArray) =
        completable { mediaManager.upload(byteArray).callback(uploadCallback(it)) }

    override fun downImage(imageId: String) = throw UnsupportedOperationException()

    override fun seekImageId() = throw UnsupportedOperationException()

    private fun uploadCallback(it: CompletableEmitter) =
        object : UploadCallback {
            /**
             * Called when a requests completes successfully.
             *
             * @param requestId Id of the request sending this callback.
             * @param resultData Result data about the newly uploaded resource.
             */
            override fun onSuccess(requestId: String, resultData: MutableMap<Any?, Any?>) {
                it.onComplete()
            }

            /**
             * Called on upload progress updates.
             *
             * @param requestId Id of the request sending this callback.
             * @param bytes Total bytes uploaded so far.
             * @param totalBytes Total bytes to upload (if known).
             */
            override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) = Unit

            /**
             * Called when a request fails with a recoverable error and is rescheduled to a later time.
             * This is useful to update UI (e.g hide progress notifications), otherwise this callback can be ignored.
             *
             * @param requestId Id of the request sending this callback.
             * @param error Error object containing technical description and code.
             */
            override fun onReschedule(requestId: String, error: ErrorInfo) = Unit

            /**
             * Called when a request encounters an error.
             *
             * @param requestId Id of the request sending this callback.
             * @param error Error object containing description and code.
             */
            override fun onError(requestId: String, error: ErrorInfo) =
                it.onError(InternalError("Code: ${error.code} Description: ${error.description}"))

            /**
             * Called when a request starts uploading.
             *
             * @param requestId Id of the request sending this callback.
             */
            override fun onStart(requestId: String) = Unit
        }
}
