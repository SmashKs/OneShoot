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

package smash.ks.com.data.models

import com.google.firebase.database.PropertyName
import smash.ks.com.ext.const.DEFAULT_INT
import smash.ks.com.ext.const.DEFAULT_STR

data class AlbumModel(
    var title: String = DEFAULT_STR,
    var author: String = DEFAULT_STR,
    var comments: Int = DEFAULT_INT,
    var likes: Int = DEFAULT_INT,
    @get:PropertyName(FIREBASE_PROPERTY_TAG) @set:PropertyName(FIREBASE_PROPERTY_TAG)
    var tags: Tags = mapOf(),
    @get:PropertyName(FIREBASE_PROPERTY_POST_DATE) @set:PropertyName(FIREBASE_PROPERTY_POST_DATE)
    var postDate: String = DEFAULT_STR,
    @get:PropertyName(FIREBASE_PROPERTY_URI) @set:PropertyName(FIREBASE_PROPERTY_URI)
    var uris: Uris = mapOf()
) : Model {
    companion object {
        private const val FIREBASE_PROPERTY_TAG = "tag"
        private const val FIREBASE_PROPERTY_POST_DATE = "post date"
        private const val FIREBASE_PROPERTY_URI = "uri"
    }
}
