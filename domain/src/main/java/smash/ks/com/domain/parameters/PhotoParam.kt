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

import smash.ks.com.domain.Tag
import smash.ks.com.ext.const.DEFAULT_STR
import smash.ks.com.ext.date.toFormatedString
import java.util.Arrays
import java.util.Date
import java.util.UUID.randomUUID

data class PhotoParam(
    var uri: String = DEFAULT_STR,
    var imageBytes: ByteArray = byteArrayOf(),
    var author: String = DEFAULT_STR,
    var title: String = DEFAULT_STR,
    var tags: List<Tag> = emptyList(),
    var uploadDate: Date = Date()
) : Parameterable {
    companion object {
        const val PARAM_URI = "uri"
        const val PARAM_AUTHOR = "author"
        const val PARAM_TITLE = "title"
        const val PARAM_TAGS = "tag"
        const val PARAM_UPLOAD_DATE = "post date"
    }

    override fun toParameter() = throw UnsupportedOperationException()

    override fun toAnyParameter() = hashMapOf(
        PARAM_URI to mapOf(randomUUID().toString() to uri),
        PARAM_AUTHOR to author,
        PARAM_TITLE to title,
        PARAM_TAGS to tags.map { it to it }.toMap(),
        PARAM_UPLOAD_DATE to uploadDate.toFormatedString()
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PhotoParam

        if (uri != other.uri) return false
        if (!Arrays.equals(imageBytes, other.imageBytes)) return false
        if (author != other.author) return false
        if (title != other.title) return false
        if (tags != other.tags) return false
        if (uploadDate != other.uploadDate) return false

        return true
    }

    override fun hashCode(): Int {
        var result = uri.hashCode()

        result = 31 * result + Arrays.hashCode(imageBytes)
        result = 31 * result + author.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + tags.hashCode()
        result = 31 * result + uploadDate.hashCode()

        return result
    }
}
