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

package smash.ks.com.data

import smash.ks.com.domain.executors.ThreadExecutor
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadFactory
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit.SECONDS

/**
 * Decorated [ThreadPoolExecutor]
 */
class JobExecutor : ThreadExecutor {
    companion object {
        private const val INITIAL_POOL_SIZE = 3
        private const val MAX_POOL_SIZE = 5
        // Sets the amount of time an idle thread waits before terminating
        private const val KEEP_ALIVE_TIME = 10
        // Sets the Time Unit to seconds
        private val KEEP_ALIVE_TIME_UNIT = SECONDS
    }

    // TODO(jieyi): 2018/03/13 To use DI here.
    private val workQueue by lazy { LinkedBlockingQueue<Runnable>() }
    private val threadFactory by lazy { JobThreadFactory() }
    private val threadPoolExecutor by lazy {
        ThreadPoolExecutor(INITIAL_POOL_SIZE,
                           MAX_POOL_SIZE,
                           KEEP_ALIVE_TIME.toLong(),
                           KEEP_ALIVE_TIME_UNIT,
                           workQueue,
                           threadFactory)
    }

    override fun execute(runnable: Runnable) = threadPoolExecutor.execute(runnable)

    private class JobThreadFactory : ThreadFactory {
        companion object {
            private const val THREAD_NAME = "ks_thread_"
        }

        private var counter = 0

        override fun newThread(runnable: Runnable) = Thread(runnable, THREAD_NAME + counter++)
    }
}
