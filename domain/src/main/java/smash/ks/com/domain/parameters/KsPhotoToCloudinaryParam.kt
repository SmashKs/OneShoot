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

package smash.ks.com.domain.parameters

import com.devrapid.kotlinshaver.cast
import java.util.Arrays

data class KsPhotoToCloudinaryParam(
    val imageByteArray: ByteArray = byteArrayOf()
) : Parameterable {
    companion object {
        const val PARAM_BYTE_ARRAY = "image byte array"
    }

    override fun toParameter() = throw UnsupportedOperationException()

    override fun toAnyParameter() = hashMapOf(PARAM_BYTE_ARRAY to cast<Any>(imageByteArray))

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        val casted = cast<KsPhotoToCloudinaryParam>(other)

        if (!Arrays.equals(imageByteArray, casted.imageByteArray)) return false

        return true
    }

    override fun hashCode() = Arrays.hashCode(imageByteArray)
}
