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

package smash.ks.com.oneshoot.classifiers

import android.graphics.Bitmap
import android.graphics.RectF

/**
 * Generic interface for interacting with different recognition engines.
 */
interface Classifier {
    companion object {
        private const val PERCENT_NUMBER = 100f
    }

    val statString: String

    /**
     * An immutable result returned by a Classifier describing what was recognized.
     */
    class Recognition(
        /**
         * A unique identifier for what has been recognized. Specific to the class, not the instance of
         * the object.
         */
        val id: String?,
        /**
         * Display name for the recognition.
         */
        val title: String?,
        /**
         * A sortable score for how good the recognition is relative to others. Higher should be better.
         */
        val confidence: Float?,
        /** Optional location within the source image for the location of the recognized object.  */
        location: RectF? = null
    ) {
        var location = location
            get() = RectF(field)

        override fun toString() = buildString {
            id?.let { append("[$it] ") }
            title?.let { append("$it ") }
            confidence?.let { append(String.format("(%.1f%%) ", confidence * PERCENT_NUMBER)) }
            location?.let { append("$location ") }
        }.trim { it <= ' ' }
    }

    fun recognizeImage(bitmap: Bitmap): List<Recognition>

    fun enableStatLogging(debug: Boolean)

    fun close()
}
