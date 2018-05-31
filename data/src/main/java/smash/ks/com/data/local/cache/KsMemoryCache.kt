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

package smash.ks.com.data.local.cache

import com.devrapid.kotlinshaver.currentTime
import com.devrapid.kotlinshaver.isNotNull
import com.ks.smash.ext.const.takeIfDefault

open class KsMemoryCache : KsCache() {
    companion object {
        private const val CAPACITY = 200
        private const val THRESHOLD = 24L * 60 * 60 * 1000 // a day (time unit is ms)

        const val CATEGORY_KS = 43
    }

    /**
     * Keeping the searched result in the cache memory.
     *
     * Structure is as like:
     * ======================================================
     * [Category WATCH_LIST(KEY), List(VALUE)]
     *   ↳ Triple<Parameters, Cached Time, Cached Data(KS)>
     *   ↳ Triple<Parameters, Cached Time, Cached Data(KS)>
     *   ↳ Triple<Parameters, Cached Time, Cached Data(KS)>
     *   ↳ Triple<Parameters, Cached Time, Cached Data(KS)>
     * [Category FEATURE_WATCH_LIST(KEY), List(VALUE)]
     *   ↳ Triple<Parameters, Cached Time, Cached Data(SMASH)>
     *   ↳ Triple<Parameters, Cached Time, Cached Data(SMASH)>
     */
    protected val memory by lazy { HashMap<Int, MutableList<Triple<Any, Long, Any>>>(CAPACITY) }

    override fun isDirty(which: Int, params: Any) =
        if (isCached(which, params)) {
            isHit(which, params)?.let { currentTime - it.second > THRESHOLD } != false
        }
        else {
            true
        }

    override fun isCached(which: Int, params: Any) = isHit(which, params).isNotNull()

    override fun refreshOrCache(which: Int, params: Any, obj: Any?) {
        // TODO(jieyi): 2018/03/08 Currently here is no refresh function.
        if (isHit(which, params).isNotNull()) {
            // *** [isHit(which, params)] must not null object.
            val originData = isHit(which, params)!!.third

            memory[which]!!.add(Triple(params, currentTime, obj.takeIf(Any?::isNotNull) ?: originData))
        }
        else {
            obj.takeIf(Any?::isNotNull)?.let {
                val data = Triple(params, currentTime, it)

                memory[which]?.add(data) ?: memory.put(which, mutableListOf(data))
            }
        }
    }

    override fun obtainCachedObj(which: Int, params: Any) = isHit(which, params)?.third

    override fun clearCache(which: Int, params: Any?) =
        which.takeIfDefault { memory.clear() } ?: let { memory[which]?.remove(params).isNotNull(); Unit }

    override fun describeMemory() = memory.toString()

    protected fun isHit(which: Int, params: Any) = memory[which]?.find { it.first == params }
}

