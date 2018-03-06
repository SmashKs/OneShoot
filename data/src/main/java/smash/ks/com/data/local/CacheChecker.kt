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

package smash.ks.com.data.local

interface CacheChecker {
    /**
     * Checks if an element [] exists in the cache.
     *
     * @param which check a column is cached.
     * @return true if the element is cached, otherwise false.
     */
    fun isCached(which: String): Boolean

    /**
     * Checks if the cache is expired.
     *
     * @param which check a column is expired.
     * @return true, the cache is expired, otherwise false.
     */
    fun isExpired(which: String): Boolean

    /**
     * Keep the last caching the item time.
     *
     * @param which
     */
    fun keepLastCacheTime(which: String)
}