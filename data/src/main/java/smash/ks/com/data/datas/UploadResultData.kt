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

package smash.ks.com.data.datas

import com.devrapid.kotlinshaver.castOrNull
import smash.ks.com.ext.const.DEFAULT_INT
import smash.ks.com.ext.const.DEFAULT_STR

/**
 * The data object for extracting from the uploading information.
 */
data class UploadResultData(
    var format: String = DEFAULT_STR,
    var resourceType: String = DEFAULT_STR,
    var secureUrl: String = DEFAULT_STR,
    var createdAt: String = DEFAULT_STR,
    var publicId: String = DEFAULT_STR,
    var width: Int = DEFAULT_INT,
    var height: Int = DEFAULT_INT,
    var placeholder: Boolean = false,
    var tags: List<Tag> = emptyList()
) : Data {
    companion object {
        private const val DATA_KEY_FORMAT = "format"
        private const val DATA_KEY_RESOURCE_TYPE = "resource_type"
        private const val DATA_KEY_SECURE_URL = "secure_url"
        private const val DATA_KEY_CREATED_AT = "created_at"
        private const val DATA_KEY_PUBLIC_ID = "public_id"
        private const val DATA_KEY_WIDTH = "width"
        private const val DATA_KEY_HEIGHT = "height"
        private const val DATA_KEY_PLACEHOLDER = "placeholder"
        private const val DATA_KEY_TAGS = "tags"

        fun extract(resultData: MutableMap<Any?, Any?>) =
            UploadResultData(castOrNull<String>(resultData[DATA_KEY_FORMAT]).orEmpty(),
                             castOrNull<String>(resultData[DATA_KEY_RESOURCE_TYPE]).orEmpty(),
                             castOrNull<String>(resultData[DATA_KEY_SECURE_URL]).orEmpty(),
                             castOrNull<String>(resultData[DATA_KEY_CREATED_AT]).orEmpty(),
                             castOrNull<String>(resultData[DATA_KEY_PUBLIC_ID]).orEmpty(),
                             castOrNull<Int>(resultData[DATA_KEY_WIDTH]) ?: DEFAULT_INT,
                             castOrNull<Int>(resultData[DATA_KEY_HEIGHT]) ?: DEFAULT_INT,
                             castOrNull<Boolean>(resultData[DATA_KEY_PLACEHOLDER]) ?: false,
                             castOrNull<List<Tag>>(resultData[DATA_KEY_TAGS]).orEmpty())
    }
}
